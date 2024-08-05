package com.backendbloggingapp.blog_app.repository;

import com.backendbloggingapp.blog_app.models.Category;
import com.backendbloggingapp.blog_app.models.Post;
import com.backendbloggingapp.blog_app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {

    List<Post> findByUser(User user);// custom method
    List<Post> findByCategory(Category category);

    @Query("select p from Post p where p.title like :key")
    List<Post> searchByTitle(@Param("key") String title);
}
