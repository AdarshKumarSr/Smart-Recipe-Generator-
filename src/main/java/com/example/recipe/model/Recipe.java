package com.example.recipe.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

// If using Lombok, you can add @Data and @AllArgsConstructor etc.
// For clarity, I will include full constructors/getters/setters.

@Document(collection = "recipes")
public class Recipe {
    @Id
    private String id;
    private String name;
    private List<String> ingredients;
    private int timeMinutes;
    private String difficulty;
    private List<String> dietTags;
    private int calories;
    private int protein; // grams

    public Recipe() {}

    public Recipe(String id, String name, List<String> ingredients, int timeMinutes, String difficulty, List<String> dietTags, int calories, int protein) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.timeMinutes = timeMinutes;
        this.difficulty = difficulty;
        this.dietTags = dietTags;
        this.calories = calories;
        this.protein = protein;
    }

    // getters & setters
    // ... generate in IntelliJ (or use Lombok)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<String> getIngredients() { return ingredients; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }
    public int getTimeMinutes() { return timeMinutes; }
    public void setTimeMinutes(int timeMinutes) { this.timeMinutes = timeMinutes; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public List<String> getDietTags() { return dietTags; }
    public void setDietTags(List<String> dietTags) { this.dietTags = dietTags; }
    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }
    public int getProtein() { return protein; }
    public void setProtein(int protein) { this.protein = protein; }
}
