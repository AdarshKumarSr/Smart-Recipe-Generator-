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

    // ---------------------------------------------------------
    // AI direct
    // ---------------------------------------------------------
    @PostMapping("/ai-recipe")
    public ResponseEntity<?> aiRecipe(@RequestBody Map<String, Object> req) {

        List<String> ing = (List<String>) req.get("ingredients");

        String raw = service.generateStructuredRecipeFromGemini(ing);

        Map<String, Object> parsed = safeParse(raw);
        parsed.put("ai", true);   // <---- IMPORTANT!!!!

        return ResponseEntity.ok(parsed);
    }


    // ---------------------------------------------------------
    // FIND → Option 2 logic (ingredient-based search + AI fallback)
    // ---------------------------------------------------------
    @PostMapping("/find")
    public ResponseEntity<?> findRecipes(
            @RequestBody FindRequest req,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) String diet,
            @RequestParam(defaultValue = "5") int top) {

        List<String> ing = new ArrayList<>();

        if (req.getIngredients() != null && !req.getIngredients().isEmpty())
            ing.addAll(req.getIngredients());
        else if (req.getIngredientsText() != null && !req.getIngredientsText().isBlank())
            ing.addAll(service.parseTextIngredients(req.getIngredientsText()));

        // 1️⃣ Check if ingredients are FOOD
        boolean edible = service.isLikelyFood(ing);

        if (!edible) {
            // → PROPER behavior: skip DB, show AI button
            return ResponseEntity.ok(Map.of("aiSuggested", true));
        }

        // 2️⃣ Normal DB search (keeps previous logic)
        List<MatchResult> results = service.findMatches(ing, cuisine, diet, top);

        if (!results.isEmpty()) {
            return ResponseEntity.ok(results);
        }

        // 3️⃣ DB empty → frontend shows AI button
        return ResponseEntity.ok(Map.of("aiSuggested", true));
    }

    /**
     * ADVANCED FILTER
     *
     * This endpoint supports an optional 'ingredients' query param.
     * If 'ingredients' is present the search will first narrow to recipes that
     * contain at least one of those ingredients and then apply the remaining filters.
     *
     * Examples:
     *  GET /api/recipes/filter?ingredients=egg,tomato&cuisine=Indian&diet=vegetarian
     *  GET /api/recipes/filter?cuisine=Indian&tag=spicy    --> behaves like previous advancedFilterRecipes
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Recipe>> filterRecipes(
            @RequestParam(required = false) String ingredients, // optional comma/space separated
            @RequestParam(required = false) String diet,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) Integer maxTime,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) String tag,
            @RequestParam(defaultValue = "5") int top) {

        if (ingredients != null && !ingredients.isBlank()) {
            // parse text into list using service helper (normalizes, splits)
            List<String> ingList = service.parseTextIngredients(ingredients);
            return ResponseEntity.ok(
                    service.advancedFilterWithIngredients(ingList, diet, difficulty, maxTime, cuisine, minRating, tag, top)
            );
        }

        // fallback: old behavior (filter without ingredients)
        return ResponseEntity.ok(
                service.advancedFilterRecipes(diet, difficulty, maxTime, cuisine, minRating, tag, top)
        );
    }

    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.ok(service.getAllRecipes());
    }
}
