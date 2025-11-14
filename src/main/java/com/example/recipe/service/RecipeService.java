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
                return true;

            String prompt = """
                You are a strict food validator.
                Determine if MOST of these items are real, edible food ingredients.

                Reply ONLY:
                yes
                or
                no

                Items: %s
                """.formatted(ingredients);

            var res = geminiClient.models.generateContent("gemini-2.0-flash", prompt, null);
            String out = res.text().trim().toLowerCase();

            return out.equals("yes");

        } catch (Exception e) {
            return true; // fail-safe → allow DB
        }
    }

    // --------------------------------------------------------------------
    // STRICT GEMINI JSON RECIPE GENERATOR
    // --------------------------------------------------------------------
    public String generateStructuredRecipeFromGemini(List<String> ingredients) {

        try {
            String prompt = """
You MUST output ONLY valid JSON. No text before/after JSON.

IMAGE:
Return BASE64 JPEG inside "imageBase64". No URLs.

FORMAT:
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
            String json = response.text();

            json = json.replace("```json", "")
                    .replace("```", "")
                    .trim();

            if (json.contains("{") && json.contains("}")) {
                json = json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1);
            }

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
            """.formatted(String.join(",", ingredients));
        }
    }

    // --------------------------------------------------------------------
    // NORMALIZE
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
        return Arrays.stream(text.split(","))
                .map(String::trim)
                .map(this::normalize)
                .filter(x -> !x.isEmpty())
                .distinct()
                .toList();
    }

    // --------------------------------------------------------------------
    // DB SEARCH — no strict threshold (OPTION 2)
    // --------------------------------------------------------------------
    public List<MatchResult> findMatches(List<String> ingredients, String cuisine, String diet, int limit) {

        Set<String> set = ingredients.stream().map(this::normalize).collect(Collectors.toSet());

        return repo.findAll().stream()
                .filter(r -> cuisine == null || r.getCuisine().equalsIgnoreCase(cuisine))
                .filter(r -> diet == null || r.getDietTags().contains(diet.toLowerCase()))
                .map(r -> {
                    long match = r.getIngredients().stream()
                            .map(this::normalize)
                            .filter(set::contains)
                            .count();

                    double ingredientScore = (double) match / r.getIngredients().size();
                    double ratingScore = r.getRating() / 5.0;

                    double finalScore = (ingredientScore * 0.7) + (ratingScore * 0.3);

                    return new MatchResult(r, finalScore);
                })
                .sorted(Comparator.comparingDouble(MatchResult::getScore).reversed())
                .limit(limit)
                .toList();
    }

    public List<Recipe> advancedFilterRecipes(String diet, String difficulty, Integer maxTime,
                                              String cuisine, Double minRating, String tag, int top) {

        return repo.findAll().stream()
                .filter(r -> diet == null || r.getDietTags().contains(diet.toLowerCase()))
                .filter(r -> difficulty == null || r.getDifficulty().equalsIgnoreCase(difficulty))
                .filter(r -> maxTime == null || r.getTimeMinutes() <= maxTime)
                .filter(r -> cuisine == null || r.getCuisine().equalsIgnoreCase(cuisine))
                .filter(r -> minRating == null || r.getRating() >= minRating)
                .filter(r -> tag == null || r.getTags().contains(tag.toLowerCase()))
                .sorted(Comparator.comparingDouble(Recipe::getRating).reversed())
                .limit(top)
                .toList();
    }

    public List<Recipe> getAllRecipes() {
        return repo.findAll();
    }
}
