package com.example.recipe.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindRequest {

    private List<String> ingredients = new ArrayList<>();
    private String ingredientsText;

    public FindRequest() {}

    // ---------------------------
    // INGREDIENT LIST (SAFE)
    // ---------------------------
    public List<String> getIngredients() {
        if (ingredients == null) return new ArrayList<>();
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        if (ingredients == null) {
            this.ingredients = new ArrayList<>();
        } else {
            // normalize + trim everything
            this.ingredients = ingredients.stream()
                    .filter(i -> i != null && !i.trim().isEmpty())
                    .map(i -> i.trim().toLowerCase())
                    .distinct()
                    .toList();
        }
    }


    // ---------------------------
    // INGREDIENT TEXT (SAFE)
    // ---------------------------
    public String getIngredientsText() {
        return ingredientsText;
    }

    public void setIngredientsText(String ingredientsText) {
        this.ingredientsText = ingredientsText;

        if (ingredientsText != null && !ingredientsText.trim().isEmpty()) {
            List<String> split = Arrays.stream(ingredientsText.split("[,\\s]+"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(String::toLowerCase)
                    .toList();

            // Merge text → list only if list is empty
            if ((this.ingredients == null || this.ingredients.isEmpty()) && !split.isEmpty()) {
                this.ingredients = new ArrayList<>(split);
            }
        }
    }
}
