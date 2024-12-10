package com.theoeve.ecommerce_project.controller;


import com.theoeve.ecommerce_project.model.Product;
import com.theoeve.ecommerce_project.model.User;
import com.theoeve.ecommerce_project.service.ProductService;
import com.theoeve.ecommerce_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/products/{prodId}")
    public ResponseEntity<Product> getProduct(@PathVariable int prodId) {
        Product product = productService.getProductById(prodId);

        if (product != null)
            return new ResponseEntity<>(product, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile,
                                        @RequestHeader(value = "Authorization", required = false) String token) {

        System.out.println("TOKEN PRODUCT CONTROLLER: " + token);
        if (token == null || !token.startsWith("Bearer ")) {
            return new ResponseEntity<>("Token manquant ou incorrect", HttpStatus.BAD_REQUEST);
        }

        try {
            String tokenWithoutBearer = token.replace("Bearer ", "");
            System.out.println("Header Authorization: " + tokenWithoutBearer);

            String username = userService.getUsernameFromToken(tokenWithoutBearer);
            System.out.println("Header username: " + username);

            User user = userService.getUserByUsername(username);
            System.out.println("User: " + user);

            if (user == null) {
                return new ResponseEntity<>("Utilisateur introuvable", HttpStatus.UNAUTHORIZED);
            }

            product.setUser(user);

            Product savedProduct = productService.addProduct(product, imageFile);

            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erreur lors de l'ajout du produit: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/product/{prodId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int prodId) {
        try {
            Product product = productService.getProductById(prodId);

            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null); // Produit non trouvé
            }

            byte[] imageFile = product.getImageDate();
            String imageType = product.getImageType();

            if (imageFile == null || imageType == null) {
                imageFile = loadPlaceholderImage();
                imageType = "image/jpg";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(imageType))
                    .body(imageFile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    private byte[] loadPlaceholderImage() {
        try {
            InputStream is = getClass().getResourceAsStream("/static/images/placeholder.jpg");
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement de l'image placeholder", e);
        }
    }


    @PutMapping("/products/{prodId}")
    public ResponseEntity<?> updateProduct(@PathVariable int prodId,
                                           @RequestPart Product product,
                                           @RequestPart(required = false) MultipartFile imageFile) {
        try {
            Product updatedProduct = productService.updateProduct(prodId, product, imageFile);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>("Erreur lors de la mise à jour de l'image : " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur interne : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/product/{prodId}")
    public ResponseEntity<String> deleteProduct(@PathVariable int prodId) {
        Product product = productService.getProductById(prodId);
        if (product != null) {
            productService.deleteProduct(prodId);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } else
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);

    }
}
