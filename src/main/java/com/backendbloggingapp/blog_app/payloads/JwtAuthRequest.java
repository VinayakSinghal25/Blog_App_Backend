package com.backendbloggingapp.blog_app.payloads;

import lombok.Data;

@Data
public class JwtAuthRequest {

    private String username;//this will be email basically

    private String password;
}
