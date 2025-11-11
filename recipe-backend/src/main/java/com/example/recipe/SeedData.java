package com.example.recipe;

import com.example.recipe.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class SeedData {
    public static List<Recipe> createSeed() {
        List<Recipe> list = new ArrayList<>();

        list.add(new Recipe(null, "Tomato Egg Curry",
                List.of("tomato","egg","onion","garlic","oil","salt"),
                25, "easy", List.of("non-vegetarian"), 280, 14));
        list.add(new Recipe(null, "Simple Omelette",
                List.of("egg","salt","pepper","butter","onion"),
                10, "easy", List.of("non-vegetarian"), 190, 12));
        list.add(new Recipe(null, "Tomato Soup",
                List.of("tomato","onion","garlic","butter","salt"),
                20, "easy", List.of("vegetarian"), 150, 4));
        list.add(new Recipe(null, "Vegetable Fried Rice",
                List.of("rice","carrot","peas","onion","soy sauce","egg"),
                30, "medium", List.of("vegetarian"), 350, 8));
        list.add(new Recipe(null, "Egg Bhurji",
                List.of("egg","onion","tomato","turmeric","salt","oil"),
                15, "easy", List.of("non-vegetarian"), 210, 13));
        list.add(new Recipe(null, "Pasta with Tomato Sauce",
                List.of("pasta","tomato","garlic","olive oil","salt"),
                25, "easy", List.of("vegetarian"), 400, 10));
        list.add(new Recipe(null, "Shakshuka",
                List.of("tomato","egg","onion","bell pepper","spices"),
                30, "medium", List.of("non-vegetarian"), 300, 12));
        list.add(new Recipe(null, "Egg Fried Noodles",
                List.of("noodles","egg","soy sauce","onion","carrot"),
                20, "easy", List.of("non-vegetarian"), 360, 14));
        list.add(new Recipe(null, "Tomato Sandwich",
                List.of("bread","tomato","butter","salt","pepper"),
                10, "easy", List.of("vegetarian"), 250, 6));
        list.add(new Recipe(null, "Tomato & Chickpea Stew",
                List.of("tomato","chickpeas","onion","garlic","spices"),
                40, "medium", List.of("vegetarian","gluten-free"), 330, 12));
        list.add(new Recipe(null, "Spanish Tortilla",
                List.of("potato","egg","onion","olive oil","salt"),
                35, "medium", List.of("non-vegetarian"), 420, 10));
        list.add(new Recipe(null, "Egg Salad",
                List.of("egg","mayonnaise","lettuce","salt","pepper"),
                10, "easy", List.of("non-vegetarian"), 260, 11));
        list.add(new Recipe(null, "Tomato Rice",
                List.of("rice","tomato","onion","garlic","oil","salt"),
                25, "easy", List.of("vegetarian"), 320, 7));
        list.add(new Recipe(null, "Egg Curry (South Indian)",
                List.of("egg","onion","tomato","curry leaves","spices"),
                30, "medium", List.of("non-vegetarian"), 290, 13));
        list.add(new Recipe(null, "Bruschetta",
                List.of("bread","tomato","garlic","olive oil","basil"),
                15, "easy", List.of("vegetarian"), 210, 5));

        return list;
    }
}
