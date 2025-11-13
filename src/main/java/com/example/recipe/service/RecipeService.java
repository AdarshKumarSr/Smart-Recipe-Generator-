package com.example.recipe.service;

import com.example.recipe.dto.FindRequest;
import com.example.recipe.dto.MatchResult;
import com.example.recipe.model.Recipe;
import com.example.recipe.repository.RecipeRepository;
import com.google.genai.Client;
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

    // ----------------------------------------------------------
    // Extract both formats safely
    // ----------------------------------------------------------
    public List<String> extractIngredients(FindRequest req) {
        List<String> list = new ArrayList<>();

        if (req.getIngredients() != null && !req.getIngredients().isEmpty()) {
            list.addAll(req.getIngredients());
        }

        if (req.getIngredientsText() != null && !req.getIngredientsText().isBlank()) {
            list.addAll(parseTextIngredients(req.getIngredientsText()));
        }

        return list.stream()
                .map(this::normalize)
                .distinct()
                .toList();
    }

    // ----------------------------------------------------------
    // Gemini Strict JSON Recipe Generator
    // ----------------------------------------------------------
    public String generateStructuredRecipeFromGemini(List<String> ingredients) {
        try {
            String prompt = """
You are a JSON generator.
Your ONLY output must be strict valid JSON.

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
    "imageUrl": "string",
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

NO Markdown, NO ```.

Ingredients = %s
""".formatted(ingredients);

            var res = geminiClient.models.generateContent("gemini-2.0-flash", prompt, null);
            String text = res.text();

            return text
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .replace("“", "\"")
                    .replace("”", "\"")
                    .trim();

        } catch (Exception e) {
            return "{\"error\":\"" + e.getMessage().replace("\"","'") + "\"}";
        }
    }

    // ----------------------------------------------------------
    // Normalize
    // ----------------------------------------------------------
    private String normalize(String s) {
        if (s == null) return null;
        s = s.toLowerCase().trim();
        if (s.endsWith("es")) return s.substring(0, s.length() - 2);
        if (s.endsWith("s")) return s.substring(0, s.length() - 1);
        return s;
    }

    // ----------------------------------------------------------
    // Parse Text Ingredients
    // ----------------------------------------------------------
    public List<String> parseTextIngredients(String text) {
        return Arrays.stream(text.split(","))
                .map(String::trim)
                .filter(v -> !v.isEmpty())
                .map(this::normalize)
                .toList();
    }

    // ----------------------------------------------------------
    // DB Match Only (AI handled in controller)
    // ----------------------------------------------------------
    public List<MatchResult> findBestMatchesFromDB(List<String> ingredients,
                                                   String cuisine,
                                                   String diet,
                                                   int limit) {

        Set<String> normalized = ingredients.stream().map(this::normalize).collect(Collectors.toSet());

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

            double ingredientScore = r.getIngredients().isEmpty() ? 0 : (double) matched / r.getIngredients().size();
            double ratingScore = r.getRating() / 5.0;

            double finalScore = ingredientScore * 0.7 + ratingScore * 0.3;

            if (finalScore > 0.2) {
                scored.add(new MatchResult(r, finalScore));
            }
        }

        return scored.stream()
                .sorted(Comparator.comparingDouble(MatchResult::getScore).reversed())
                .limit(limit)
                .toList();
    }

    // ----------------------------------------------------------
    // Filters
    // ----------------------------------------------------------
    public List<Recipe> advancedFilterRecipes(String diet, String difficulty, Integer maxTime,
                                              String cuisine, Double minRating, String tag, int top) {

        return repo.findAll().stream()
                .filter(r -> diet == null || r.getDietTags().contains(diet.toLowerCase()))
                .filter(r -> difficulty == null || r.getDifficulty().equalsIgnoreCase(difficulty))
                .filter(r -> maxTime == null || r.getTimeMinutes() <= maxTime)
                .filter(r -> cuisine == null || (r.getCuisine() != null && r.getCuisine().equalsIgnoreCase(cuisine)))
                .filter(r -> minRating == null || r.getRating() >= minRating)
                .filter(r -> tag == null || r.getTags().stream().anyMatch(t -> t.equalsIgnoreCase(tag)))
                .sorted(Comparator.comparingDouble(Recipe::getRating).reversed())
                .limit(top)
                .toList();
    }

    public List<Recipe> getAllRecipes() {
        return repo.findAll();
    }
}
