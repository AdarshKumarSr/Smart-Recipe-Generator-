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

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("recipe service alive ✅");
    }

    // ============================================================
    // SAFE JSON PARSER (AI fallback)
    // ============================================================
    private Map<String, Object> safeParse(String raw) {
        try {
            return mapper.readValue(raw, Map.class);
        } catch (Exception e) {

            Map<String, Object> fallback = new HashMap<>();
            Map<String, Object> recipe = new HashMap<>();

            recipe.put("id", "AI-FALLBACK");
            recipe.put("name", "AI Generated Dish");
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

    // ============================================================
    // DIRECT AI RECIPE
    // ============================================================
    @PostMapping("/ai-recipe")
    public ResponseEntity<?> aiRecipe(@RequestBody Map<String, Object> req) {

        List<String> ing = (List<String>) req.get("ingredients");

        String raw = service.generateStructuredRecipeFromGemini(ing);
        Map<String, Object> parsed = safeParse(raw);

        parsed.put("ai", true);
        return ResponseEntity.ok(parsed);
    }

    // ============================================================
    // UNIFIED FIND LOGIC (DB + AI fallback)
    // ============================================================
    @PostMapping("/find")
    public ResponseEntity<?> findRecipes(
            @RequestBody FindRequest req,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) String diet,
            @RequestParam(defaultValue = "5") int top) {

        // ------------------------------
        // EXTRACT CLEAN INGREDIENTS
        // ------------------------------
        List<String> ing = new ArrayList<>();

        if (req.getIngredients() != null && !req.getIngredients().isEmpty()) {
            ing.addAll(req.getIngredients());
        } else if (req.getIngredientsText() != null && !req.getIngredientsText().isBlank()) {
            ing.addAll(service.parseTextIngredients(req.getIngredientsText()));
        }

        // If nothing entered → treat as non-edible → AI
        if (ing.isEmpty()) {
            return ResponseEntity.ok(Map.of("aiSuggested", true));
        }

        // ------------------------------
        // CHECK IF INGREDIENTS ARE REAL FOOD
        // ------------------------------
        boolean edible = service.isLikelyFood(ing);

        if (!edible) {
            return ResponseEntity.ok(Map.of("aiSuggested", true));
        }

        // ------------------------------
        // DB SEARCH
        // ------------------------------
        List<MatchResult> results = service.findMatches(ing, cuisine, diet, top);

        // If DB empty → AI
        if (results.isEmpty()) {
            return ResponseEntity.ok(Map.of("aiSuggested", true));
        }

        // If all scores are 0 → meaningless match → AI
        boolean allZero = results.stream().allMatch(r -> r.getScore() < 0.01);
        if (allZero) {
            return ResponseEntity.ok(Map.of("aiSuggested", true));
        }

        // Valid DB results
        return ResponseEntity.ok(results);
    }

    // ============================================================
    // ADVANCED FILTER
    // ============================================================
    @GetMapping("/filter")
    public ResponseEntity<List<Recipe>> filterRecipes(
            @RequestParam(required = false) String ingredients,
            @RequestParam(required = false) String diet,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) Integer maxTime,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) String tag,
            @RequestParam(defaultValue = "5") int top) {

        if (ingredients != null && !ingredients.isBlank()) {
            List<String> ingList = service.parseTextIngredients(ingredients);
            return ResponseEntity.ok(
                    service.advancedFilterWithIngredients(ingList, diet, difficulty, maxTime, cuisine, minRating, tag, top)
            );
        }

        return ResponseEntity.ok(
                service.advancedFilterRecipes(diet, difficulty, maxTime, cuisine, minRating, tag, top)
        );
    }

    // ============================================================
    // GET ALL
    // ============================================================
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.ok(service.getAllRecipes());
    }
}
