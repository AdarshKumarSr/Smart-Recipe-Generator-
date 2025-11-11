package com.example.recipe.controller;

import com.example.recipe.dto.FindRequest;
import com.example.recipe.dto.MatchResult;
import com.example.recipe.model.Recipe;
import com.example.recipe.service.RecipeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService service;

    public RecipeController(RecipeService service) {
        this.service = service;
    }

    // 🩺 Simple health check
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("recipe service alive");
    }

    /**
     * 🍳 Ingredient-based recipe finder
     * Accepts:
     * - {"ingredients": ["tomato","egg"]}
     * - {"ingredientsText": "tomato, egg"}
     * Returns top N matches (default = 5)
     */
    @PostMapping("/find")
    public ResponseEntity<List<MatchResult>> findRecipes(
            @RequestBody FindRequest req,
            @RequestParam(defaultValue = "5") int top
    ) {
        List<String> ingredients = new ArrayList<>();

        if (req.getIngredients() != null && !req.getIngredients().isEmpty()) {
            ingredients.addAll(req.getIngredients());
        }

        if ((req.getIngredients() == null || req.getIngredients().isEmpty()) && req.getIngredientsText() != null) {
            ingredients.addAll(service.parseTextIngredients(req.getIngredientsText()));
        } else if (req.getIngredientsText() != null && !req.getIngredientsText().isBlank()) {
            // combine both forms if both provided
            ingredients.addAll(service.parseTextIngredients(req.getIngredientsText()));
        }

        List<MatchResult> result = service.findBestMatches(ingredients, top);
        return ResponseEntity.ok(result);
    }

    /**
     * 🧩 Filter recipes by diet, difficulty, or time
     * Example:
     * /api/recipes/filter?diet=vegetarian&difficulty=easy&maxTime=30
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Recipe>> filterRecipes(
            @RequestParam(required = false) String diet,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) Integer maxTime
    ) {
        List<Recipe> filtered = service.filterRecipes(diet, difficulty, maxTime);
        return ResponseEntity.ok(filtered);
    }
}
