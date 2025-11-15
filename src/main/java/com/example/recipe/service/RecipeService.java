package com.example.recipe.service;

import com.example.recipe.dto.MatchResult;
import com.example.recipe.model.Recipe;
import com.example.recipe.repository.RecipeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository repo;
    private final Client geminiClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public RecipeService(RecipeRepository repo, Client geminiClient) {
        this.repo = repo;
        this.geminiClient = geminiClient;
    }

    // --------------------------------------------------------------------
    // AI CHECK → Are ingredients real edible food?
    // --------------------------------------------------------------------
    public boolean isLikelyFood(List<String> ingredients) {
        try {
            if (ingredients == null || ingredients.isEmpty())
                return false; // ❗ Empty → AI will be used instead

            String prompt = """
            You are a strict food ingredient validator.

            For EACH item below, determine if it is a **real edible ingredient**.

            If ANY item is not real food, reply only with:
            no

            If ALL items are edible ingredients, reply only:
            yes

            Items: %s
            """.formatted(ingredients);

            var res = geminiClient.models.generateContent("gemini-2.0-flash", prompt, null);
            String out = res.text().trim().toLowerCase();

            return out.equals("yes");

        } catch (Exception e) {
            return false; // ❗ Fallback to AI suggestion instead of DB if an error occurs
        }
    }

    // --------------------------------------------------------------------
    // STRICT GEMINI JSON RECIPE GENERATOR
    // --------------------------------------------------------------------
    public String generateStructuredRecipeFromGemini(List<String> ingredients) {

        try {
            String prompt = """
            You MUST output ONLY valid JSON. No text before or after the JSON.

            IMPORTANT RULES:
            - If you cannot follow the format exactly, return:
              { "recipe": null, "score": 0 }
            - If ANY ingredient is unsafe, fictional, or non-edible:
              return { "recipe": null, "score": 0 }

            IMAGE RULE:
            - "imageBase64" may be a valid base64 JPEG OR an empty string.
            - Never return URLs.
            - Never include markdown code blocks.

            JSON FORMAT:
            {
              "recipe": {
                "id": "string",
                "name": "string",
                "ingredients": ["string"],
                "timeMinutes": number,
                "difficulty": "easy" | "medium" | "hard",
                "dietTags": ["string"],
                "calories": number,
                "protein": number,
                "instructions": "string",
                "imageBase64": "string",
                "youtubeLink": "string",
                "cuisine": "string",
                "rating": number,
                "reviewsCount": number,
                "tags": ["string"],
                "prepTime": "string",
                "servingSize": "string"
              },
              "score": number
            }

            INGREDIENTS: %s
            """.formatted(ingredients);

            var response = geminiClient.models.generateContent("gemini-2.0-flash", prompt, null);
            String json = response.text().trim();

            // Clean the response to ensure it's valid JSON
            json = json.replace("```json", "")
                    .replace("```", "")
                    .trim();

            if (json.contains("{") && json.contains("}")) {
                json = json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1);
            }

            // Validate JSON structure
            new ObjectMapper().readTree(json); // validate
            return json;

        } catch (Exception e) {
            return """
            {
              "recipe": {
                "id": "AI-FALLBACK",
                "name": "AI Generated Dish",
                "ingredients": ["%s"],
                "timeMinutes": 20,
                "difficulty": "easy",
                "dietTags": [],
                "calories": 300,
                "protein": 10,
                "instructions": "No instructions available.",
                "imageBase64": "",
                "youtubeLink": "",
                "cuisine": "Fusion",
                "rating": 4.0,
                "reviewsCount": 20,
                "tags": [],
                "prepTime": "10 minutes",
                "servingSize": "2 servings"
              },
              "score": 0.5
            }
            """.formatted(String.join(",", ingredients)); // Fallback response for invalid recipes
        }
    }

    // --------------------------------------------------------------------
    // NORMALIZE INGREDIENTS
    // --------------------------------------------------------------------
    private String normalize(String s) {
        if (s == null) return null;
        s = s.toLowerCase().trim();
        if (s.endsWith("es")) s = s.substring(0, s.length() - 2);
        else if (s.endsWith("s")) s = s.substring(0, s.length() - 1);
        return s;
    }

    public List<String> parseTextIngredients(String text) {
        if (text == null || text.isBlank()) return Collections.emptyList();
        return Arrays.stream(text.split("[,\\s]+"))
                .map(String::trim)
                .map(this::normalize)
                .filter(x -> !x.isEmpty())
                .distinct()
                .toList();
    }

    // --------------------------------------------------------------------
    // DB SEARCH — No strict threshold (OPTION 2)
    // --------------------------------------------------------------------
    public List<MatchResult> findMatches(List<String> ingredients, String cuisine, String diet, int limit) {

        Set<String> set = ingredients.stream().map(this::normalize).collect(Collectors.toSet());

        return repo.findAll().stream()
                .filter(r -> cuisine == null || r.getCuisine().equalsIgnoreCase(cuisine))
                .filter(r -> diet == null || r.getDietTags().stream().anyMatch(d -> d.equalsIgnoreCase(diet)))
                .map(r -> {
                    long match = r.getIngredients().stream()
                            .map(this::normalize)
                            .filter(set::contains)
                            .count();

                    double ingredientScore = r.getIngredients().isEmpty() ? 0.0 : (double) match / r.getIngredients().size();
                    double ratingScore = r.getRating() / 5.0;

                    double finalScore = (ingredientScore * 0.7) + (ratingScore * 0.3);

                    return new MatchResult(r, finalScore);
                })
                .sorted(Comparator.comparingDouble(MatchResult::getScore).reversed())
                .limit(limit)
                .toList();
    }

    // --------------------------------------------------------------------
    // Filtering with ingredients (New Logic)
    // --------------------------------------------------------------------
    public List<Recipe> advancedFilterWithIngredients(
            List<String> ingredients,
            String diet,
            String difficulty,
            Integer maxTime,
            String cuisine,
            Double minRating,
            String tag,
            int top) {

        // Normalize ingredient set
        Set<String> ingSet = ingredients == null
                ? Collections.emptySet()
                : ingredients.stream().map(this::normalize).collect(Collectors.toSet());

        return repo.findAll().stream()

                // Must contain at least one of the provided ingredients
                .filter(r -> {
                    if (ingSet.isEmpty()) return true;
                    return r.getIngredients().stream()
                            .map(this::normalize)
                            .anyMatch(ingSet::contains);
                })

                // Then apply the same filters as before
                .filter(r -> diet == null || diet.isBlank() ||
                        r.getDietTags().stream()
                                .anyMatch(d -> d.equalsIgnoreCase(diet)))

                .filter(r -> difficulty == null || difficulty.isBlank() ||
                        r.getDifficulty().equalsIgnoreCase(difficulty))

                .filter(r -> maxTime == null || r.getTimeMinutes() <= maxTime)

                .filter(r -> cuisine == null || cuisine.isBlank() ||
                        r.getCuisine().equalsIgnoreCase(cuisine))

                .filter(r -> minRating == null || r.getRating() >= minRating)

                .filter(r -> tag == null || tag.isBlank() ||
                        r.getTags().stream()
                                .anyMatch(t -> t.equalsIgnoreCase(tag)))

                .sorted(Comparator.comparingDouble(Recipe::getRating).reversed())
                .limit(top)
                .toList();
    }

    // --------------------------------------------------------------------
    // Fallback filtering
    // --------------------------------------------------------------------
    public List<Recipe> advancedFilterRecipes(
            String diet,
            String difficulty,
            Integer maxTime,
            String cuisine,
            Double minRating,
            String tag,
            int top) {

        return repo.findAll().stream()

                // Diet filter (flexible match)
                .filter(r -> diet == null || diet.isBlank() ||
                        r.getDietTags().stream()
                                .anyMatch(d -> d.equalsIgnoreCase(diet)))

                // Difficulty filter
                .filter(r -> difficulty == null || difficulty.isBlank() ||
                        r.getDifficulty().equalsIgnoreCase(difficulty))

                // Max time filter
                .filter(r -> maxTime == null || r.getTimeMinutes() <= maxTime)

                // Cuisine filter
                .filter(r -> cuisine == null || cuisine.isBlank() ||
                        r.getCuisine().equalsIgnoreCase(cuisine))

                // Rating filter
                .filter(r -> minRating == null || r.getRating() >= minRating)

                // Tag filter
                .filter(r -> tag == null || tag.isBlank() ||
                        r.getTags().stream()
                                .anyMatch(t -> t.equalsIgnoreCase(tag)))

                .sorted(Comparator.comparingDouble(Recipe::getRating).reversed())
                .limit(top)
                .toList();
    }

    public List<Recipe> getAllRecipes() {
        return repo.findAll();
    }
}
