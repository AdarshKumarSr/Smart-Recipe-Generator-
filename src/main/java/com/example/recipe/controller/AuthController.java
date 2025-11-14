package com.example.recipe.controller;

import com.example.recipe.model.User;
import com.example.recipe.repository.UserRepository;
import com.example.recipe.service.JwtService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    private final UserRepository userRepo;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepo, JwtService jwtService) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }

    // STEP 1: Frontend sends the AUTH CODE here
    @PostMapping("/google/callback")
    public ResponseEntity<?> googleCallback(@RequestBody Map<String, String> body) throws Exception {

        String code = body.get("code");
        if (code == null) {
            return ResponseEntity.status(400).body(Map.of("error", "Missing authorization code"));
        }

        // STEP 2: Exchange AUTH CODE for TOKENS
        RestTemplate rest = new RestTemplate();
        String tokenUrl = "https://oauth2.googleapis.com/token";

        Map<String, String> params = Map.of(
                "code", code,
                "client_id", clientId,
                "client_secret", clientSecret,
                "redirect_uri", redirectUri,
                "grant_type", "authorization_code"
        );

        ResponseEntity<String> tokenResponse = rest.postForEntity(tokenUrl, params, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode tokenJson = mapper.readTree(tokenResponse.getBody());

        String idToken = tokenJson.get("id_token").asText();

        // STEP 3: Decode user info
        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + idToken;
        JsonNode userInfo = rest.getForObject(userInfoUrl, JsonNode.class);

        String email = userInfo.get("email").asText();
        String name = userInfo.get("name").asText();
        String picture = userInfo.get("picture").asText();

        // STEP 4: Save / fetch user
        User user = userRepo.findByEmail(email)
                .orElseGet(() -> userRepo.save(new User(email, name, picture)));

        // STEP 5: Issue JWT
        String jwt = jwtService.generateToken(user);

        return ResponseEntity.ok(Map.of(
                "token", jwt,
                "user", user
        ));
    }
}
