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
    // DIRECT AI ENDPOINT (optional)
    // ---------------------------------------------------------
    @PostMapping("/ai-recipe")
    public ResponseEntity<?> generateAIRecipe(@RequestBody Map<String, Object> request) {
        try {
            List<String> ingredients = (List<String>) request.get("ingredients");

            if (ingredients == null || ingredients.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Ingredients missing"));
            }

            String cleanJson = service.generateStructuredRecipeFromGemini(ingredients);

            Map<String, Object> json = mapper.readValue(cleanJson, Map.class);

            return ResponseEntity.ok(json);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Gemini failed", "details", e.getMessage()));
        }
    }


    // ---------------------------------------------------------
    // INGREDIENT-BASED SEARCH (WITH AI FALLBACK)
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

        // 1️⃣ TRY DATABASE MATCHES
        List<MatchResult> matches = service.findBestMatchesWithFilters(ingredients, cuisine, diet, top);

        // 2️⃣ IF DB FOUND RESULTS → RETURN THEM
        if (!matches.isEmpty()) {
            return ResponseEntity.ok(matches);
        }

        // 3️⃣ AI FALLBACK — DB returned nothing
        try {
            String aiJson = service.generateStructuredRecipeFromGemini(ingredients);

            Map<String, Object> parsed = mapper.readValue(aiJson, Map.class);

            return ResponseEntity.ok(parsed);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "AI fallback failed", "details", e.getMessage()));
        }
    }


    // ---------------------------------------------------------
    // ADVANCED FILTER ENDPOINT
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
        List<Recipe> filtered = service.advancedFilterRecipes(
                diet, difficulty, maxTime, cuisine, minRating, tag, top
        );
        return ResponseEntity.ok(filtered);
    }

    // ---------------------------------------------------------
    // GET ALL RECIPES
    // ---------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.ok(service.getAllRecipes());
    }
}
