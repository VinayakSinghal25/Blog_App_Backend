package com.backendbloggingapp.blog_app.repository;

import com.backendbloggingapp.blog_app.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {


}
