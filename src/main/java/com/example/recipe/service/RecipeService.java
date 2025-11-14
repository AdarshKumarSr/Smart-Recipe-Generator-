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

    // -------------------------------------------------------------
    // 🔥 AI: Determine if ingredients are real edible food
    // -------------------------------------------------------------
    public boolean isLikelyFood(List<String> ingredients) {
        try {
            if (ingredients == null || ingredients.isEmpty())
                return true;

            String prompt = """
                You are a strict food validator.
                Determine if MOST of these items are real, edible food ingredients.

                Return ONLY:
                yes
                or
                no

                Items: %s
                """.formatted(ingredients);

            var res = geminiClient.models.generateContent("gemini-2.0-flash", prompt, null);
            String out = res.text().trim().toLowerCase();

            return out.equals("yes") || out.contains("yes");

        } catch (Exception e) {
            // fail-safe: assume food so we don't block legitimate requests when AI check fails
            return true;
        }
    }


    // -------------------------------------------------------------
    // STRICT GEMINI GENERATOR (JSON + BASE64 IMAGE)
    // -------------------------------------------------------------
    public String generateStructuredRecipeFromGemini(List<String> ingredients) {
        try {
            // Build prompt
            String prompt = """
You are an AI that MUST output ONLY valid JSON and nothing else.

STRICT RULES:
- Output EXACTLY one JSON object.
- No markdown, no code fences.
- No text outside JSON.
- Escape internal quotes properly.

IMAGE RULE:
Generate a REALISTIC dish photo and return it as BASE64 JPEG inside imageBase64.
Do NOT return URLs.

OUTPUT FORMAT:
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
            String content = response.text();

            // Clean code fences if present (safe)
            content = content.replaceAll("(?i)```json", "");
            content = content.replaceAll("(?i)```", "");
            content = content.trim();

            // Attempt to extract JSON from first { to last }
            if (content.contains("{") && content.contains("}")) {
                content = content.substring(content.indexOf("{"), content.lastIndexOf("}") + 1);
            }

            // Validate JSON strictly using Jackson
            mapper.readTree(content); // will throw if invalid

            // valid JSON -> return as-is
            return content;

        } catch (Exception e) {
            // On any failure, return a safe fallback JSON produced by ObjectMapper
            try {
                Map<String, Object> fallback = new HashMap<>();
                Map<String, Object> recipe = new HashMap<>();

                recipe.put("id", "AI-FALLBACK");
                recipe.put("name", "AI Generated Dish");
                // ingredients: safe list; if original list null -> empty
                recipe.put("ingredients", ingredients == null ? List.of() : ingredients);
                recipe.put("timeMinutes", 20);
                recipe.put("difficulty", "easy");
                recipe.put("dietTags", List.of());
                recipe.put("calories", 300);
                recipe.put("protein", 10);
                recipe.put("instructions", "No instructions available.");
                recipe.put("imageBase64", "");
                recipe.put("youtubeLink", "");
                recipe.put("cuisine", "Fusion");
                recipe.put("rating", 4.0);
                recipe.put("reviewsCount", 20);
                recipe.put("tags", List.of());
                recipe.put("prepTime", "10 minutes");
                recipe.put("servingSize", "2 servings");

                fallback.put("recipe", recipe);
                fallback.put("score", 0.5);

                return mapper.writeValueAsString(fallback);

            } catch (Exception ex) {
                // Last resort: minimal JSON string
                return "{\"recipe\":{\"id\":\"AI-FALLBACK\",\"name\":\"AI Generated Dish\"},\"score\":0.5}";
            }
        }
    }

    // -------------------------------------------------------------
    // NORMALIZE INGREDIENT
    // -------------------------------------------------------------
    private String normalize(String s) {
        if (s == null) return null;
        s = s.toLowerCase().trim();
        if (s.endsWith("es")) s = s.substring(0, s.length() - 2);
        else if (s.endsWith("s")) s = s.substring(0, s.length() - 1);
        return s;
    }

    // -------------------------------------------------------------
    // PARSE TEXT INGREDIENTS
    // -------------------------------------------------------------
    public List<String> parseTextIngredients(String text) {
        if (text == null || text.isBlank()) return Collections.emptyList();
        return Arrays.stream(text.split(","))
                .map(String::trim)
                .map(this::normalize)
                .filter(x -> !x.isEmpty())
                .distinct()
                .toList();
    }

    // -------------------------------------------------------------
    // DB MATCHER WITH ≥ 0.5 THRESHOLD (STRICT)
    // -------------------------------------------------------------
    public List<MatchResult> findBestMatchesWithFilters(
            List<String> ingredients,
            String cuisine,
            String diet,
            int limit
    ) {

        Set<String> normalized = (ingredients == null)
                ? Collections.emptySet()
                : ingredients.stream().map(this::normalize).collect(Collectors.toSet());

        boolean edible = isLikelyFood(ingredients);

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

            double ingredientScore = (double) matched / Math.max(1, r.getIngredients().size());
            double ratingScore = r.getRating() / 5.0;
            double finalScore = ingredientScore * 0.7 + ratingScore * 0.3;

            // Strict: only accept DB results when input is likely-food AND ingredient coverage >= 0.5
            if (edible && ingredientScore >= 0.5) {
                scored.add(new MatchResult(r, finalScore));
            }
        }

        return scored.stream()
                .sorted(Comparator.comparingDouble(MatchResult::getScore).reversed())
                .limit(limit)
                .toList();
    }

    // -------------------------------------------------------------
    // FILTER SYSTEM
    // -------------------------------------------------------------
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
