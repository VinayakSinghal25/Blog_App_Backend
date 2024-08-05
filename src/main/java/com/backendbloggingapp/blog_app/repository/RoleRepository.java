package com.backendbloggingapp.blog_app.repository;

import com.backendbloggingapp.blog_app.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
}
