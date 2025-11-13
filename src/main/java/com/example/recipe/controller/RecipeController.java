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

    // --------------------------------------------------
    // HEALTH CHECK
    // --------------------------------------------------
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("recipe service alive ✅");
    }

    // --------------------------------------------------
    // AI RECIPE (MANUAL BUTTON FROM FRONTEND)
    // --------------------------------------------------
    @PostMapping("/ai-recipe")
    public ResponseEntity<?> generateAIRecipe(@RequestBody Map<String, Object> request) {
        try {
            List<String> ingredients = (List<String>) request.get("ingredients");

            if (ingredients == null || ingredients.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Ingredients missing"));
            }

            String cleanJson = service.generateStructuredRecipeFromGemini(ingredients);

            Map<String, Object> parsed = mapper.readValue(cleanJson, Map.class);

            return ResponseEntity.ok(Map.of(
                    "ai", true,
                    "recipe", parsed.get("recipe"),
                    "score", parsed.get("score")
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Gemini failed", "details", e.getMessage()));
        }
    }


    // --------------------------------------------------
    // INGREDIENT SEARCH + AI FALLBACK INDICATOR
    // --------------------------------------------------
    @PostMapping("/find")
    public ResponseEntity<?> findRecipes(
            @RequestBody FindRequest req,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) String diet,
            @RequestParam(defaultValue = "5") int top
    ) {

        List<String> ingredients = service.extractIngredients(req);

        List<MatchResult> matches = service.findBestMatchesFromDB(ingredients, cuisine, diet, top);

        // IF DB FOUND RESULTS → return them
        if (!matches.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "ai", false,
                    "results", matches
            ));
        }

        // DB empty — frontend should show AI button
        return ResponseEntity.ok(Map.of(
                "ai", false,
                "results", List.of()
        ));
    }

    // --------------------------------------------------
    // FILTER SEARCH
    // --------------------------------------------------
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

    // --------------------------------------------------
    // GET ALL RECIPES
    // --------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.ok(service.getAllRecipes());
    }
}
