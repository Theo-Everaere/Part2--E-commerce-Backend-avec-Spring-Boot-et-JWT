package com.theoeve.ecommerce_project.controller;

import com.theoeve.ecommerce_project.model.JwtResponse;
import com.theoeve.ecommerce_project.model.Product;
import com.theoeve.ecommerce_project.model.User;
import com.theoeve.ecommerce_project.service.JwtService;
import com.theoeve.ecommerce_project.service.MyUserDetailsService;
import com.theoeve.ecommerce_project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private MyUserDetailsService myUserDetailsService;


    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest servletRequest) {
        return (CsrfToken) servletRequest.getAttribute("_csrf");
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        String token = userService.verify(user);

        if (token.equals("Echec de la connexion")) {
            System.out.println("Echec de la connexion pour l'utilisateur : " + user.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Echec de la connexion");
        }

        User loggedInUser = userService.getUserByUsername(user.getUsername());
        System.out.println("Connexion réussie, token généré : " + token);
        return ResponseEntity.ok(new JwtResponse(token, loggedInUser));
    }

    @GetMapping("/user/{userId}/products")
    public List<Product> getProductsOfUserById(@PathVariable int userId) {
        return userService.getAllProductsByUserId(userId);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Supprimer "Bearer " du token

            String username = jwtService.extractUsername(token);

            try {
                UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

                if (jwtService.validateToken(token, userDetails)) {
                    User user = userService.getUserByUsername(username); // Récupérer les détails utilisateur
                    return ResponseEntity.ok(Map.of(
                            "isValid", true,
                            "user", user
                    ));
                }
            } catch (UsernameNotFoundException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "isValid", false,
                        "message", "Utilisateur non trouvé"
                ));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "isValid", false,
                "message", "Token invalide ou expiré"
        ));
    }


}
