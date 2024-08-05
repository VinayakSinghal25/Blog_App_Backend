package com.backendbloggingapp.blog_app.service.Impl;

import com.backendbloggingapp.blog_app.exceptions.ResourceNotFoundException;
import com.backendbloggingapp.blog_app.models.Category;
import com.backendbloggingapp.blog_app.models.Post;
import com.backendbloggingapp.blog_app.models.User;
import com.backendbloggingapp.blog_app.payloads.CategoryDto;
import com.backendbloggingapp.blog_app.payloads.PostDto;
import com.backendbloggingapp.blog_app.payloads.PostResponse;
import com.backendbloggingapp.blog_app.repository.CategoryRepository;
import com.backendbloggingapp.blog_app.repository.PostRepository;
import com.backendbloggingapp.blog_app.repository.UserRepository;
import com.backendbloggingapp.blog_app.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String POST_PREFIX_KEY ="post";

    @Override
    public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User ", "User id", userId));

        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "category id ", categoryId));

        Post post = this.modelMapper.map(postDto, Post.class);
        post.setImageName("default.png");
        post.setAddedDate(new Date());
        post.setUser(user);
        post.setCategory(category);

        Post newPost = this.postRepository.save(post);
        PostDto newPostDto = this.modelMapper.map(newPost, PostDto.class);
        List<PostDto> posts=new ArrayList<>();
        posts.add(newPostDto);

        return newPostDto;
    }


    private void pushToRedisByCategory(List<PostDto> posts){
        if(posts!=null){

            CategoryDto category = posts.get(0).getCategory();
            String redisKey = POST_PREFIX_KEY + category.getCategoryId(); // Use category ID for the key
            redisTemplate.opsForList().rightPushAll(redisKey, posts);
            redisTemplate.expire(redisKey, 10, TimeUnit.MINUTES);

        }
    }
    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post ", "post id", postId));



        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

        if(postDto.getImageName() != null){
            post.setImageName(postDto.getImageName());
        }


        Post updatedPost = this.postRepository.save(post);
        return this.modelMapper.map(updatedPost, PostDto.class);
    }

    @Override
    public void deletePost(Integer postId) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post ", "post id", postId));

        this.postRepository.delete(post);
    }

    @Override
    public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable p = PageRequest.of(pageNumber, pageSize, sort);//sort karne ki inner implementation hai
        // khud hee sort kardeta hai,humein sirf field batani hai

        Page<Post> pagePost = this.postRepository.findAll(p);

        List<Post> allPosts = pagePost.getContent();

        List<PostDto> postDtos = allPosts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();

        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pagePost.getNumber());//page post mein hee sab kuchh hai,wahan se le sakte hain
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalElements(pagePost.getTotalElements());

        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setLastPage(pagePost.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(Integer postId) {

        String redisKey = POST_PREFIX_KEY + postId;
        List<PostDto> postWithId = redisTemplate.opsForList().range(redisKey, 0, -1);
        if(!postWithId.isEmpty()){


            return postWithId.get(0);
        }
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));

        PostDto postDto = this.modelMapper.map(post, PostDto.class);

        pushToRedisById(postDto, postId);

        return postDto;
    }
    private void pushToRedisById(PostDto post ,Integer postId){
        if(post!=null){

            String redisKey = POST_PREFIX_KEY + postId; // Use category ID for the key
            redisTemplate.opsForList().rightPushAll(redisKey, post);
            redisTemplate.expire(redisKey, 10, TimeUnit.MINUTES);

        }
    }

    @Override
    public List<PostDto> getPostsByCategory(Integer categoryId) {
        Category cat = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));

        String redisKey = POST_PREFIX_KEY + categoryId;

        List<PostDto> postWithCategory = redisTemplate.opsForList().range(redisKey, 0, -1);

        if(!postWithCategory.isEmpty()){


            return postWithCategory;
        }
        List<Post> posts = this.postRepository.findByCategory(cat);



        List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
        pushToRedisByCategory(postDtos);

        return postDtos;
    }

    @Override
    public List<PostDto> getPostsByUser(Integer userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User ", "userId ", userId));

        String redisKey = POST_PREFIX_KEY + userId;

        List<PostDto> postWithUserId = redisTemplate.opsForList().range(redisKey, 0, -1);
        if(!postWithUserId.isEmpty()){


            return postWithUserId;
        }

        List<Post> posts = this.postRepository.findByUser(user);

        List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
        pushToRedisByUserId(postDtos,userId);
        return postDtos;
    }
    private void pushToRedisByUserId(List<PostDto> posts,Integer userId){
        if(posts!=null){

            String redisKey = POST_PREFIX_KEY + userId; // Use category ID for the key
            redisTemplate.opsForList().rightPushAll(redisKey, posts);
            redisTemplate.expire(redisKey, 10, TimeUnit.MINUTES);

        }
    }
    @Override
    public List<PostDto> searchPosts(String keyword) {
        List<Post> posts = this.postRepository.searchByTitle("%" + keyword + "%");
        List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        return postDtos;
    }
}
