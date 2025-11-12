package com.example.recipe;

import com.example.recipe.model.Recipe;
import com.example.recipe.repository.RecipeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootApplication
public class RecipeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeBackendApplication.class, args);
	}

	// ✅ Global CORS configuration
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**")
						.allowedOrigins(
								"http://localhost:5173",               // local Vite dev server
								"https://cuisinex-frontend.vercel.app" // deployed frontend (future)
						)
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowedHeaders("*");
			}
		};
	}

	// ✅ Seed data if MongoDB is empty
	@Bean
	public CommandLineRunner initDatabase(RecipeRepository repo) {
		return args -> {
			if (repo.count() == 0) {
				List<Recipe> seed = SeedData.createSeed();
				repo.saveAll(seed);
				System.out.println("✅ Seeded " + seed.size() + " recipes into MongoDB Atlas.");
			} else {
				System.out.println("ℹ️ Recipes already present: " + repo.count());
			}
		};
	}
}
