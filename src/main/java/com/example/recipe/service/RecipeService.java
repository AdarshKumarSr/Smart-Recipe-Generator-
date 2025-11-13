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

    // -------------------------------------------------------------
    // 1️⃣ STRICT JSON Structured Gemini Generator
    // -------------------------------------------------------------
    public String generateStructuredRecipeFromGemini(List<String> ingredients) {
        try {
            String prompt = """
You are a JSON generator.
Your ONLY output must be strict valid JSON.
Do NOT include markdown, comments, backticks, or explanations.

Return EXACTLY this structure:

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

The ingredients are: %s

Rules:
- STRICT JSON only  
- Do NOT return ```json or ```  
- Do NOT return markdown  
- All numbers must be numbers  
- rating: 3.5–5  
- calories: 150–800  
- protein: 5–40  
""".formatted(ingredients);

            var response = geminiClient.models.generateContent("gemini-2.0-flash", prompt, null);

            String text = response.text();

            // CLEAN unwanted characters
            text = text
                    .replaceAll("(?i)```json", "")
                    .replaceAll("(?i)```", "")
                    .replace("“", "\"")
                    .replace("”", "\"")
                    .trim();

            System.out.println("🧠 CLEAN AI JSON:\n" + text);

            return text;

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"" + e.getMessage().replace("\"", "'") + "\"}";
        }
    }

    // -------------------------------------------------------------
    // 2️⃣ Ingredient Normalizer
    // -------------------------------------------------------------
    private String normalize(String s) {
        if (s == null) return null;
        s = s.toLowerCase().trim();
        if (s.endsWith("es")) s = s.substring(0, s.length() - 2);
        else if (s.endsWith("s")) s = s.substring(0, s.length() - 1);
        return s;
    }

    // -------------------------------------------------------------
    // 3️⃣ Parse comma-separated text
    // -------------------------------------------------------------
    public List<String> parseTextIngredients(String text) {
        if (text == null || text.isBlank()) return Collections.emptyList();

        return Arrays.stream(text.split(","))
                .map(String::trim)
                .filter(t -> !t.isEmpty())
                .map(this::normalize)
                .distinct()
                .toList();
    }

    // -------------------------------------------------------------
    // 4️⃣ MAIN MATCHING LOGIC + FIXED THRESHOLD
    // -------------------------------------------------------------
    public List<MatchResult> findBestMatchesWithFilters(List<String> ingredients,
                                                        String cuisine,
                                                        String diet,
                                                        int limit) {

        Set<String> normalized = ingredients.stream()
                .map(this::normalize)
                .collect(Collectors.toSet());

        List<Recipe> all = repo.findAll().stream()
                .filter(r -> cuisine == null ||
                        (r.getCuisine() != null && r.getCuisine().equalsIgnoreCase(cuisine)))
                .filter(r -> diet == null ||
                        (r.getDietTags() != null && r.getDietTags().contains(diet.toLowerCase())))
                .toList();

        List<MatchResult> scored = new ArrayList<>();

        for (Recipe r : all) {

            long matched = r.getIngredients().stream()
                    .map(this::normalize)
                    .filter(normalized::contains)
                    .count();

            // ❗ NEW FIX — skip if zero matched ingredients
            if (matched == 0) continue;

            double ingredientScore = r.getIngredients().isEmpty()
                    ? 0.0
                    : (double) matched / r.getIngredients().size();

            double ratingScore = r.getRating() / 5.0;
            double finalScore = ingredientScore * 0.7 + ratingScore * 0.3;

            // ❗ NEW FIX — require strong match (60%+)
            if (finalScore >= 0.6) {
                scored.add(new MatchResult(r, finalScore));
            }
        }

        // return sorted list
        return scored.stream()
                .sorted(Comparator.comparingDouble(MatchResult::getScore).reversed())
                .limit(limit)
                .toList();
    }


    // -------------------------------------------------------------
    // 5️⃣ Advanced Filters
    // -------------------------------------------------------------
    public List<Recipe> advancedFilterRecipes(String diet,
                                              String difficulty,
                                              Integer maxTime,
                                              String cuisine,
                                              Double minRating,
                                              String tag,
                                              int top) {

        return repo.findAll().stream()
                .filter(r -> diet == null || r.getDietTags().contains(diet.toLowerCase()))
                .filter(r -> difficulty == null || r.getDifficulty().equalsIgnoreCase(difficulty))
                .filter(r -> maxTime == null || r.getTimeMinutes() <= maxTime)
                .filter(r -> cuisine == null ||
                        (r.getCuisine() != null && r.getCuisine().equalsIgnoreCase(cuisine)))
                .filter(r -> minRating == null || r.getRating() >= minRating)
                .filter(r -> tag == null || r.getTags().stream()
                        .anyMatch(t -> t.equalsIgnoreCase(tag)))
                .sorted(Comparator.comparingDouble(Recipe::getRating).reversed())
                .limit(top)
                .toList();
    }

    // -------------------------------------------------------------
    // 6️⃣ Get All Recipes
    // -------------------------------------------------------------
    public List<Recipe> getAllRecipes() {
        return repo.findAll();
    }
}
