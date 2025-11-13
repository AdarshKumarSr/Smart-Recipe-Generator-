package com.example.recipe.service;

import com.example.recipe.dto.MatchResult;
import com.example.recipe.model.Recipe;
import com.example.recipe.repository.RecipeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository repo;
    private final Client geminiClient;

    public RecipeService(RecipeRepository repo, Client geminiClient) {
        this.repo = repo;
        this.geminiClient = geminiClient;
    }

    // ... your rest of the code ...
    public String testGeminiConnection() {
        try {
            String prompt = "Say hello from RecipeBackend as a JSON: {\"message\": \"Hello from Gemini!\"}";
            var response = geminiClient.models.generateContent("gemini-2.0-flash", prompt, null);

            String text = response.text();
            System.out.println("🧠 Gemini test response: " + text);
            return text;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
    public String generateStructuredRecipeFromGemini(List<String> ingredients) {
        try {
            String prompt = """
                Generate one recipe in **strict JSON format** with this exact structure:
                {
                  "recipe": {
                    "id": "<some unique string id>",
                    "name": "String",
                    "ingredients": ["String"],
                    "timeMinutes": 0,
                    "difficulty": "easy | medium | hard",
                    "dietTags": ["String"],
                    "calories": 0,
                    "protein": 0,
                    "instructions": "Short cooking summary",
                    "imageUrl": "https://example.com/img.jpg",
                    "youtubeLink": "https://youtube.com/...",
                    "cuisine": "String",
                    "rating": 0.0,
                    "reviewsCount": 0,
                    "tags": ["String"],
                    "prepTime": "e.g. '10 min prep, 25 min cook'",
                    "servingSize": "e.g. '3 servings'"
                  },
                  "score": 0.644
                }

                The ingredients are: %s

                Rules:
                - Only return valid JSON (no markdown, no ``` fences)
                - Keep rating realistic (3.5–5)
                - Keep calories and protein numeric
                - Cuisine should match the recipe type
                - Give short but clear instructions
                - Do not add extra keys.
                """.formatted(ingredients);

            var response = geminiClient.models.generateContent("gemini-2.0-flash", prompt, null);
            String text = response.text();

            System.out.println("🧠 Gemini Structured Recipe Response:\n" + text);
            return text;

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}";
        }
    }



    // 🧹 Normalize ingredient text: lowercase, trim, remove plurals
    private String normalize(String s) {
        if (s == null) return null;
        s = s.toLowerCase().trim();
        if (s.endsWith("es")) s = s.substring(0, s.length() - 2);
        else if (s.endsWith("s")) s = s.substring(0, s.length() - 1);
        return s;
    }

    // 🧾 Parse comma separated text into list
    public List<String> parseTextIngredients(String text) {
        if (text == null || text.isBlank()) return Collections.emptyList();
        return Arrays.stream(text.split(","))
                .map(String::trim)
                .filter(t -> !t.isEmpty())
                .map(this::normalize)
                .distinct()
                .collect(Collectors.toList());
    }

    // ✅ Main recipe finder (with AI fallback)
    public List<MatchResult> findBestMatchesWithFilters(List<String> ingredients, String cuisine, String diet, int limit) {
        Set<String> normalized = ingredients.stream()
                .map(this::normalize)
                .collect(Collectors.toSet());

        List<Recipe> all = repo.findAll().stream()
                .filter(r -> cuisine == null || (r.getCuisine() != null && r.getCuisine().equalsIgnoreCase(cuisine)))
                .filter(r -> diet == null || (r.getDietTags() != null && r.getDietTags().contains(diet.toLowerCase())))
                .toList();

        List<MatchResult> scored = new ArrayList<>();

        for (Recipe r : all) {
            long matched = r.getIngredients().stream()
                    .map(this::normalize)
                    .filter(normalized::contains)
                    .count();

            double ingredientScore = r.getIngredients().isEmpty() ? 0.0 : (double) matched / r.getIngredients().size();
            double ratingScore = (r.getRating() / 5.0);
            double finalScore = (ingredientScore * 0.7) + (ratingScore * 0.3);

            if (finalScore > 0.2) {
                scored.add(new MatchResult(r, finalScore));
            }
        }

        // 🧠 No matches? Generate from AI
        if (scored.isEmpty()) {
            System.out.println("⚠️ No local match found. Calling Gemini API...");
            Recipe aiRecipe = generateRecipeFromGemini(ingredients);
            if (aiRecipe != null) {
                return List.of(new MatchResult(aiRecipe, 0.95));
            }
        }

        return scored.stream()
                .sorted(Comparator.comparingDouble(MatchResult::getScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    // ✅ Advanced filtering
    public List<Recipe> advancedFilterRecipes(String diet, String difficulty, Integer maxTime,
                                              String cuisine, Double minRating, String tag, int top) {
        List<Recipe> all = repo.findAll();

        return all.stream()
                .filter(r -> diet == null || r.getDietTags().contains(diet.toLowerCase()))
                .filter(r -> difficulty == null || r.getDifficulty().equalsIgnoreCase(difficulty))
                .filter(r -> maxTime == null || r.getTimeMinutes() <= maxTime)
                .filter(r -> cuisine == null || (r.getCuisine() != null && r.getCuisine().equalsIgnoreCase(cuisine)))
                .filter(r -> minRating == null || r.getRating() >= minRating)
                .filter(r -> tag == null || (r.getTags() != null && r.getTags().stream()
                        .anyMatch(t -> t.equalsIgnoreCase(tag))))
                .sorted(Comparator.comparingDouble(Recipe::getRating).reversed())
                .limit(top)
                .collect(Collectors.toList());
    }

    public List<Recipe> getAllRecipes() {
        return repo.findAll();
    }

    // 🧠 Gemini SDK Integration
    private Recipe generateRecipeFromGemini(List<String> ingredients) {
        try {
            String prompt = "Generate one realistic cooking recipe in JSON format for ingredients: "
                    + ingredients + ". JSON fields must match: "
                    + "name, ingredients, timeMinutes, difficulty, dietTags, calories, protein, steps, imageUrl, youtubeUrl, cuisine, rating, reviewsCount, tags, prepTime, servingSize. "
                    + "Ensure the output is valid JSON and nothing else.";

            GenerateContentResponse response =
                    geminiClient.models.generateContent("gemini-2.5-flash", prompt, null);

            String text = response.text();
            ObjectMapper mapper = new ObjectMapper();

            // Parse Gemini JSON into Recipe object
            JsonNode recipeNode = mapper.readTree(text);
            Recipe aiRecipe = mapper.treeToValue(recipeNode, Recipe.class);

            // Save it for reuse
            repo.save(aiRecipe);

            System.out.println("✅ Gemini generated recipe: " + aiRecipe.getName());
            return aiRecipe;

        } catch (Exception e) {
            System.err.println("❌ Gemini AI error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
