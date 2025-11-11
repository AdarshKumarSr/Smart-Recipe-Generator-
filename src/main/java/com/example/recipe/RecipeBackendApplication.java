package com.example.recipe;

import com.example.recipe.model.Recipe;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import com.example.recipe.repository.RecipeRepository;

import java.util.List;


@SpringBootApplication
public class RecipeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDatabase(RecipeRepository repo) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				if (repo.count() == 0) {
					List<Recipe> seed = SeedData.createSeed();
					repo.saveAll(seed);
					System.out.println("✅ Seeded " + seed.size() + " recipes into MongoDB Atlas.");
				} else {
					System.out.println("ℹ️ Recipes already present: " + repo.count());
				}
			}
		};
	}
}
