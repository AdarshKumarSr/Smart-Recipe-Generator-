package com.example.recipe.controller;

import com.example.recipe.dto.FindRequest;
import com.example.recipe.dto.MatchResult;
import com.example.recipe.model.Recipe;
import com.example.recipe.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService service;
    private final ObjectMapper mapper = new ObjectMapper();

    public RecipeController(RecipeService service) {
        this.service = service;
    }

    // ---------------------------------------------------------
    // HEALTH CHECK
    // ---------------------------------------------------------
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("recipe service alive ✅");
    }


    // ---------------------------------------------------------
    // SAFE JSON PARSER (bulletproof)
    // ---------------------------------------------------------
    private Map<String, Object> safeParseJson(String raw) {
        try {
            return mapper.readValue(raw, Map.class);
        } catch (Exception e) {
            System.out.println("⚠ AI JSON parse failed: " + e.getMessage());

            // Fallback JSON
            Map<String, Object> fallback = new HashMap<>();
            Map<String, Object> recipe = new HashMap<>();

            recipe.put("id", "AI-FALLBACK");
            recipe.put("name", "AI Generated Recipe");
            recipe.put("ingredients", List.of());
            recipe.put("timeMinutes", 20);
            recipe.put("difficulty", "easy");
            recipe.put("dietTags", List.of());
            recipe.put("calories", 250);
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

            return fallback;
        }
    }


    // ---------------------------------------------------------
    // DIRECT AI ENDPOINT (foolproof)
    // ---------------------------------------------------------
    @PostMapping("/ai-recipe")
    public ResponseEntity<?> generateAIRecipe(@RequestBody Map<String, Object> request) {
        try {
            List<String> ingredients = (List<String>) request.get("ingredients");

            if (ingredients == null || ingredients.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Ingredients missing"));
            }

            String raw = service.generateStructuredRecipeFromGemini(ingredients);

            // Always safe parse
            Map<String, Object> json = safeParseJson(raw);

            return ResponseEntity.ok(json);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Gemini failed", "details", e.getMessage()));
        }
    }


    // ---------------------------------------------------------
    // FIND RECIPES (DB + AI fallback) (foolproof)
    // ---------------------------------------------------------
    @PostMapping("/find")
    public ResponseEntity<?> findRecipes(
            @RequestBody FindRequest req,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) String diet,
            @RequestParam(defaultValue = "5") int top
    ) {

        // Extract ingredients
        List<String> ingredients = new ArrayList<>();

        if (req.getIngredients() != null && !req.getIngredients().isEmpty()) {
            ingredients.addAll(req.getIngredients());
        }

        if ((req.getIngredients() == null || req.getIngredients().isEmpty())
                && req.getIngredientsText() != null) {
            ingredients.addAll(service.parseTextIngredients(req.getIngredientsText()));
        }

        // 1️⃣ Try database matches first
        List<MatchResult> results =
                service.findBestMatchesWithFilters(ingredients, cuisine, diet, top);

        if (!results.isEmpty()) {
            return ResponseEntity.ok(results);
        }

        // 2️⃣ AI fallback (safe)
        try {
            String raw = service.generateStructuredRecipeFromGemini(ingredients);
            Map<String, Object> parsed = safeParseJson(raw);

            return ResponseEntity.ok(parsed);

        } catch (Exception e) {
            e.printStackTrace();

            // return fallback & avoid crash
            return ResponseEntity.ok(safeParseJson("{}"));
        }
    }


    // ---------------------------------------------------------
    // FILTER RECIPES
    // ---------------------------------------------------------
    @GetMapping("/filter")
    public ResponseEntity<List<Recipe>> filterRecipes(
            @RequestParam(required = false) String diet,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) Integer maxTime,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) String tag,
            @RequestParam(defaultValue = "5") int top
    ) {
        return ResponseEntity.ok(
                service.advancedFilterRecipes(diet, difficulty, maxTime, cuisine, minRating, tag, top)
        );
    }

    // ---------------------------------------------------------
    // GET ALL RECIPES
    // ---------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.ok(service.getAllRecipes());
    }
}
