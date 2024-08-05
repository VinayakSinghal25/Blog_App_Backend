package com.backendbloggingapp.blog_app.service;

import com.backendbloggingapp.blog_app.payloads.CommentDto;

public interface CommentService {

    CommentDto createComment(CommentDto commentDto, Integer postId);

    void deleteComment(Integer commentId);
}
