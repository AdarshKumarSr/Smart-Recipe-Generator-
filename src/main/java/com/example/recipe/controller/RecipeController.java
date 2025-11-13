package com.example.recipe.controller;

import com.example.recipe.dto.FindRequest;
import com.example.recipe.dto.MatchResult;
import com.example.recipe.model.Recipe;
import com.example.recipe.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService service;

    public RecipeController(RecipeService service) {
        this.service = service;
    }

    // ✅ Health check
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("recipe service alive ✅");
    }
    // 🧠 Test Gemini API connection
    @PostMapping("/ai-recipe")
    public ResponseEntity<String> generateAIRecipe(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<String> ingredients = (List<String>) request.get("ingredients");

            if (ingredients == null || ingredients.isEmpty()) {
                return ResponseEntity.badRequest().body("❌ Please provide a list of ingredients.");
            }

            String aiResponse = service.generateStructuredRecipeFromGemini(ingredients);

            // 🧽 Cleanup extra Markdown fences if Gemini adds them
            aiResponse = aiResponse
                    .replaceAll("(?i)```json", "")
                    .replaceAll("(?i)```", "")
                    .trim();

            return ResponseEntity.ok("✅ Gemini API working! \nResponse: ```json\n" + aiResponse + "\n```");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("❌ Gemini API failed: " + e.getMessage());
        }
    }




    /**
     * 🍳 Ingredient-based recipe finder
     * Accepts:
     * - {"ingredients": ["tomato","egg"]}
     * - {"ingredientsText": "tomato, egg"}
     * Optional filters: cuisine, diet
     */
    @PostMapping("/find")
    public ResponseEntity<List<MatchResult>> findRecipes(
            @RequestBody FindRequest req,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) String diet,
            @RequestParam(defaultValue = "5") int top
    ) {
        List<String> ingredients = new ArrayList<>();

        if (req.getIngredients() != null && !req.getIngredients().isEmpty()) {
            ingredients.addAll(req.getIngredients());
        }

        if ((req.getIngredients() == null || req.getIngredients().isEmpty()) && req.getIngredientsText() != null) {
            ingredients.addAll(service.parseTextIngredients(req.getIngredientsText()));
        } else if (req.getIngredientsText() != null && !req.getIngredientsText().isBlank()) {
            ingredients.addAll(service.parseTextIngredients(req.getIngredientsText()));
        }

        List<MatchResult> result = service.findBestMatchesWithFilters(ingredients, cuisine, diet, top);
        return ResponseEntity.ok(result);
    }

    /**
     * 🧩 Filter recipes by various properties
     * Example:
     * /api/recipes/filter?diet=vegetarian&difficulty=easy&maxTime=30&cuisine=Indian&minRating=4.5&tag=spicy
     */
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
        List<Recipe> filtered = service.advancedFilterRecipes(diet, difficulty, maxTime, cuisine, minRating, tag, top);
        return ResponseEntity.ok(filtered);
    }

    // 🧠 Get all recipes
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.ok(service.getAllRecipes());
    }
}
