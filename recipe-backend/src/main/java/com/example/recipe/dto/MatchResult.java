package com.example.recipe.dto;

import com.example.recipe.model.Recipe;

public class MatchResult {
    private Recipe recipe;
    private double score; // 0.0 .. 1.0

    public MatchResult() {}
    public MatchResult(Recipe recipe, double score) {
        this.recipe = recipe;
        this.score = score;
    }
    public Recipe getRecipe() { return recipe; }
    public void setRecipe(Recipe recipe) { this.recipe = recipe; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
}
