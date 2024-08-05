package com.backendbloggingapp.blog_app.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Role {

    @Id
    private int id;
    //Here we have not auto incremented the id, because 2-4 roles only, whose id we'll keep by default

    private String name;

}