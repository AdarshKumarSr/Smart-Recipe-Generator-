package com.example.recipe.controller;

import com.example.recipe.model.User;
import com.example.recipe.repository.UserRepository;
//import com.example.recipe.security.JwtService;
import com.example.recipe.service.JwtService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${google.client.id}")
    private String googleClientId;

    private final UserRepository userRepo;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepo, JwtService jwtService) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/google", consumes = "application/json")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, Object> body) throws Exception {

//        System.out.println("🟢 Incoming body = " + body);

        String idTokenString = (String) body.get("token");
//        System.out.println("🟢 Extracted token = " + idTokenString);

        if (idTokenString == null || idTokenString.isBlank()) {
            return ResponseEntity.status(400).body(Map.of("error", "Token missing from request"));
        }

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance()
        ).setAudience(Collections.singletonList(googleClientId)).build();

        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid ID Token"));
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");

        User user = userRepo.findByEmail(email)
                .orElseGet(() -> userRepo.save(new User(email, name, picture)));

        String jwt = jwtService.generateToken(user);

        return ResponseEntity.ok(Map.of("token", jwt, "user", user));
    }

}
