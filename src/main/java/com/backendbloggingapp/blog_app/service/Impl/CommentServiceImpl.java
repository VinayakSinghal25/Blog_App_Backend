package com.backendbloggingapp.blog_app.service.Impl;

import com.backendbloggingapp.blog_app.exceptions.ResourceNotFoundException;
import com.backendbloggingapp.blog_app.models.Comment;
import com.backendbloggingapp.blog_app.models.Post;
import com.backendbloggingapp.blog_app.payloads.CommentDto;
import com.backendbloggingapp.blog_app.repository.CommentsRepository;
import com.backendbloggingapp.blog_app.repository.PostRepository;
import com.backendbloggingapp.blog_app.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "post id ", postId));

        Comment comment = this.modelMapper.map(commentDto, Comment.class);

        comment.setPost(post);

        Comment savedComment = this.commentsRepository.save(comment);

        return this.modelMapper.map(savedComment, CommentDto.class);
    }

    @Override
    public void deleteComment(Integer commentId) {
        Comment com = this.commentsRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "CommentId", commentId));
        this.commentsRepository.delete(com);
    }
}
