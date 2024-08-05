package com.backendbloggingapp.blog_app.payloads;

import lombok.Data;

@Data
public class JwtAuthResponse {

    private String token;

    private UserDto user;
}