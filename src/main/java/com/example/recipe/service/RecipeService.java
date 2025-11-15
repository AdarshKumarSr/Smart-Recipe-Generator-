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

    // ============================================================
    // FOOD VALIDATION (AI + quick heuristics)
    // ============================================================
    public boolean isLikelyFood(List<String> ingredients) {
        try {
            if (ingredients == null || ingredients.isEmpty()) {
                return true;
            }

            for (String ing : ingredients) {
                if (ing.length() > 20) return false;
                if (!ing.matches("[a-zA-Z ]+")) return false;
            }

            String prompt = """
                You are a strict FOOD ingredient validator.
                Reply ONLY: yes or no.
                
                All items must be edible ingredients.
                Items: %s
            """.formatted(ingredients);

            var res = geminiClient.models.generateContent("gemini-2.0-flash", prompt, null);
            String out = res.text().trim().toLowerCase();

            if (out.contains("yes")) return true;
            if (out.contains("no")) return false;

            return true;
        } catch (Exception e) {
            return true;
        }
    }

    // ============================================================
    // GEMINI → STRICT JSON RECIPE GENERATOR
    // ============================================================
    public String generateStructuredRecipeFromGemini(List<String> ingredients) {
        try {
            String prompt = """
    You MUST return ONLY valid JSON. Never use markdown or backticks.
    Always generate a complete recipe even if ingredients look unusual.

    DO NOT return null under any circumstances.

    "imageBase64" may be valid base64 OR empty string.
    No URLs allowed.

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

            json = cleanJson(json);
            mapper.readTree(json);

            return json;

        } catch (Exception e) {
            return fallbackRecipeJson(ingredients);
        }
    }

    private String cleanJson(String raw) {
        raw = raw.replace("```json", "").replace("```", "").trim();
        if (raw.contains("{") && raw.contains("}")) {
            return raw.substring(raw.indexOf("{"), raw.lastIndexOf("}") + 1);
        }
        return raw;
    }

    private String fallbackRecipeJson(List<String> ingredients) {
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
        """.formatted(String.join(",", ingredients));
    }

    // ============================================================
    // NORMALIZATION HELPERS
    // ============================================================
    private String normalize(String s) {
        if (s == null) return null;
        s = s.toLowerCase().trim();

        if (s.endsWith("es")) return s.substring(0, s.length() - 2);
        if (s.endsWith("s")) return s.substring(0, s.length() - 1);

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

    // ============================================================
    // DB MATCHING (OPTION 2 logic)
    // ============================================================
    public List<MatchResult> findMatches(List<String> ingredients, String cuisine, String diet, int limit) {

        Set<String> set = ingredients.stream()
                .map(this::normalize)
                .collect(Collectors.toSet());

        return repo.findAll().stream()
                .filter(r -> cuisine == null || r.getCuisine().equalsIgnoreCase(cuisine))
                .filter(r -> diet == null || r.getDietTags().stream().anyMatch(d -> d.equalsIgnoreCase(diet)))
                .map(r -> calculateMatchScore(r, set))
                .sorted(Comparator.comparingDouble(MatchResult::getScore).reversed())
                .limit(limit)
                .toList();
    }

    private MatchResult calculateMatchScore(Recipe r, Set<String> set) {
        long match = r.getIngredients().stream()
                .map(this::normalize)
                .filter(set::contains)
                .count();

        double ingredientScore =
                r.getIngredients().isEmpty() ? 0.0 : (double) match / r.getIngredients().size();

        double ratingScore = r.getRating() / 5.0;

        double finalScore = (ingredientScore * 0.7) + (ratingScore * 0.3);
        return new MatchResult(r, finalScore);
    }

    // ============================================================
    // ADVANCED FILTERS
    // ============================================================
    public List<Recipe> advancedFilterWithIngredients(
            List<String> ingredients,
            String diet,
            String difficulty,
            Integer maxTime,
            String cuisine,
            Double minRating,
            String tag,
            int top) {

        Set<String> ingSet = ingredients == null
                ? Collections.emptySet()
                : ingredients.stream().map(this::normalize).collect(Collectors.toSet());

        return repo.findAll().stream()
                .filter(r -> ingSet.isEmpty() || r.getIngredients().stream()
                        .map(this::normalize)
                        .anyMatch(ingSet::contains))
                .filter(r -> diet == null || diet.isBlank() ||
                        r.getDietTags().stream().anyMatch(d -> d.equalsIgnoreCase(diet)))
                .filter(r -> difficulty == null || difficulty.isBlank() ||
                        r.getDifficulty().equalsIgnoreCase(difficulty))
                .filter(r -> maxTime == null || r.getTimeMinutes() <= maxTime)
                .filter(r -> cuisine == null || cuisine.isBlank() ||
                        r.getCuisine().equalsIgnoreCase(cuisine))
                .filter(r -> minRating == null || r.getRating() >= minRating)
                .filter(r -> tag == null || tag.isBlank() ||
                        r.getTags().stream().anyMatch(t -> t.equalsIgnoreCase(tag)))
                .sorted(Comparator.comparingDouble(Recipe::getRating).reversed())
                .limit(top)
                .toList();
    }

    public List<Recipe> advancedFilterRecipes(
            String diet,
            String difficulty,
            Integer maxTime,
            String cuisine,
            Double minRating,
            String tag,
            int top) {

        return repo.findAll().stream()
                .filter(r -> diet == null || diet.isBlank() ||
                        r.getDietTags().stream().anyMatch(d -> d.equalsIgnoreCase(diet)))
                .filter(r -> difficulty == null || difficulty.isBlank() ||
                        r.getDifficulty().equalsIgnoreCase(difficulty))
                .filter(r -> maxTime == null || r.getTimeMinutes() <= maxTime)
                .filter(r -> cuisine == null || cuisine.isBlank() ||
                        r.getCuisine().equalsIgnoreCase(cuisine))

                .filter(r -> minRating == null || r.getRating() >= minRating)
                .filter(r -> tag == null || tag.isBlank() ||
                        r.getTags().stream().anyMatch(t -> t.equalsIgnoreCase(tag)))
                .sorted(Comparator.comparingDouble(Recipe::getRating).reversed())
                .limit(top)
                .toList();
    }

    public List<Recipe> getAllRecipes() {
        return repo.findAll();
    }
}
