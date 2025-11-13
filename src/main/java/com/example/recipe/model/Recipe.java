package com.example.recipe.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

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

    // 🆕 Newly added fields
    private String instructions;  // Detailed steps
    private String imageUrl;      // Image link of the dish
    private String youtubeLink;   // YouTube tutorial link

    // 🌎 New enrichment fields
    private String cuisine;       // e.g. "Indian", "Italian"
    private double rating;        // average rating
    private int reviewsCount;     // number of reviews
    private List<String> tags;    // e.g. ["spicy", "quick", "family-friendly"]
    private String prepTime;      // e.g. "15 min prep, 20 min cook"
    private String servingSize;   // e.g. "2 servings"

    public Recipe() {}

    // 🧩 Updated constructor including all fields
    public Recipe(String id, String name, List<String> ingredients, int timeMinutes, String difficulty,
                  List<String> dietTags, int calories, int protein,
                  String instructions, String imageUrl, String youtubeLink,
                  String cuisine, double rating, int reviewsCount,
                  List<String> tags, String prepTime, String servingSize) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.timeMinutes = timeMinutes;
        this.difficulty = difficulty;
        this.dietTags = dietTags;
        this.calories = calories;
        this.protein = protein;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
        this.youtubeLink = youtubeLink;
        this.cuisine = cuisine;
        this.rating = rating;
        this.reviewsCount = reviewsCount;
        this.tags = tags;
        this.prepTime = prepTime;
        this.servingSize = servingSize;
    }

    // 🧠 Getters & Setters
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

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getYoutubeLink() { return youtubeLink; }
    public void setYoutubeLink(String youtubeLink) { this.youtubeLink = youtubeLink; }

    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getReviewsCount() { return reviewsCount; }
    public void setReviewsCount(int reviewsCount) { this.reviewsCount = reviewsCount; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getPrepTime() { return prepTime; }
    public void setPrepTime(String prepTime) { this.prepTime = prepTime; }

    public String getServingSize() { return servingSize; }
    public void setServingSize(String servingSize) { this.servingSize = servingSize; }
}
