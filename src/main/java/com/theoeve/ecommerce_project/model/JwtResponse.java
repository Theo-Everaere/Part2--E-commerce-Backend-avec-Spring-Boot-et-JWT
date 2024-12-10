package com.theoeve.ecommerce_project.model;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private User user;


    public JwtResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }


}