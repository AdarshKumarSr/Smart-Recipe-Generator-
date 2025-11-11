package com.example.recipe.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.example.recipe.model.Recipe;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String> {
    // You can define custom queries later if needed
}