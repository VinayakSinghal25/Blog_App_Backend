package com.backendbloggingapp.blog_app.repository;

import com.backendbloggingapp.blog_app.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comment,Integer> {
}
