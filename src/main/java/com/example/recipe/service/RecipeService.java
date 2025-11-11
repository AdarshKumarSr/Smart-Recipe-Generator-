package com.example.recipe.service;

import com.example.recipe.dto.MatchResult;
import com.example.recipe.model.Recipe;
import com.example.recipe.repository.RecipeRepository;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository repo;

    public RecipeService(RecipeRepository repo) {
        this.repo = repo;
    }

    // Normalize ingredient text: lowercase, trim, singularize lightly
    private String normalize(String s) {
        if (s == null) return null;
        s = s.toLowerCase().trim();
        if (s.endsWith("es")) s = s.substring(0, s.length()-2);
        else if (s.endsWith("s")) s = s.substring(0, s.length()-1);
        return s;
    }

    // Parse comma separated text into list
    public List<String> parseTextIngredients(String text) {
        if (text == null || text.isBlank()) return Collections.emptyList();
        return Arrays.stream(text.split(","))
                .map(String::trim)
                .filter(t -> !t.isEmpty())
                .map(this::normalize)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<MatchResult> findBestMatches(List<String> userIngredients, int limit) {
        if (userIngredients == null) userIngredients = Collections.emptyList();
        // normalize incoming
        Set<String> normalized = userIngredients.stream()
                .filter(Objects::nonNull)
                .map(this::normalize)
                .collect(Collectors.toSet());

        List<Recipe> all = repo.findAll();

        List<MatchResult> scored = new ArrayList<>();

        for (Recipe r : all) {
            List<String> recIngr = r.getIngredients().stream()
                    .map(this::normalize)
                    .collect(Collectors.toList());

            long matched = recIngr.stream().filter(normalized::contains).count();
            double score = recIngr.isEmpty() ? 0.0 : (double) matched / recIngr.size();

            // add to results even if score 0 — we will sort and then limit
            scored.add(new MatchResult(r, score));
        }

        // sort desc by score then by time (optional)
        scored.sort(Comparator.comparingDouble(MatchResult::getScore).reversed());

        return scored.stream()
                .filter(m -> m.getScore() > 0.0) // only recipes with at least one match; remove if you want everything
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Recipe> filterRecipes(String diet, String difficulty, Integer maxTime) {
        List<Recipe> all = repo.findAll();

        return all.stream()
                .filter(r -> (diet == null || r.getDietTags().contains(diet.toLowerCase())))
                .filter(r -> (difficulty == null || r.getDifficulty().equalsIgnoreCase(difficulty)))
                .filter(r -> (maxTime == null || r.getTimeMinutes() <= maxTime))
                .collect(Collectors.toList());
    }

}
