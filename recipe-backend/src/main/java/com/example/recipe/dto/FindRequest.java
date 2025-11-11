package com.example.recipe.dto;

import java.util.List;

public class FindRequest {
    private List<String> ingredients;
    private String ingredientsText; // optional: comma-separated text

    public FindRequest() {}

    public List<String> getIngredients() { return ingredients; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }
    public String getIngredientsText() { return ingredientsText; }
    public void setIngredientsText(String ingredientsText) { this.ingredientsText = ingredientsText; }
}
