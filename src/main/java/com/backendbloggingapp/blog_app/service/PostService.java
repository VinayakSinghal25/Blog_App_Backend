package com.backendbloggingapp.blog_app.service;

import com.backendbloggingapp.blog_app.payloads.PostDto;
import com.backendbloggingapp.blog_app.payloads.PostResponse;

import java.util.List;

public interface PostService {

    //create

    PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);

    //update

    PostDto updatePost(PostDto postDto, Integer postId);

    // delete

    void deletePost(Integer postId);

    //get all posts
    //List<PostDto> getAllPost();
    PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    //get single post

    PostDto getPostById(Integer postId);

    //get all posts by category

    List<PostDto> getPostsByCategory(Integer categoryId);

    //get all posts by user
    List<PostDto> getPostsByUser(Integer userId);

    //search posts
    List<PostDto> searchPosts(String keyword);
}
