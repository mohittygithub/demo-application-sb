package com.tyagi.demoapplication.ServiceImpl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tyagi.demoapplication.Exception.BadRequestException;
import com.tyagi.demoapplication.Exception.ResourceNotFoundException;
import com.tyagi.demoapplication.Service.PostService;
import com.tyagi.demoapplication.model.Post;
import com.tyagi.demoapplication.model.User;
import com.tyagi.demoapplication.payload.ApiResponse;
import com.tyagi.demoapplication.payload.PostResponse;
import com.tyagi.demoapplication.payload.UserResponse;
import com.tyagi.demoapplication.repository.PostRepository;
import com.tyagi.demoapplication.repository.UserRepository;
import com.tyagi.demoapplication.utils.Utilities;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Value("${S3.BUCKET.NAME}")
    private String bucketName;

    @Value("${S3.BUCKET.URI}")
    private String bucketUri;

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final Utilities utils;
    private final AmazonS3 amazonS3;


    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, ModelMapper modelMapper, Utilities utils, AmazonS3 amazonS3) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.utils = utils;
        this.amazonS3 = amazonS3;
    }

    @Override
    public ApiResponse testPostService() {
        return null;
    }

    @Override
    public ApiResponse createPost(Post post, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        if (post.getTitle().isBlank() || post.getContent() == null)
            throw new BadRequestException("Incomplete Form Data");

        post.setUser(userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User Not Found")));
        postRepository.save(post);

        UserResponse userResponse = mapUserToUserResponse(user);

        PostResponse response = mapPostToPostResponse(post);
        response.setOwner(userResponse);

        return new ApiResponse("Post", true, post.getId(), "", Arrays.asList(response).size(), Arrays.asList(response));
    }

    @Override
    public ApiResponse updatePost(UUID postId, Post post, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Post oldPost = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post Not Found"));

        if (oldPost.getUser().getId().equals(user.getId())) {
            oldPost.setTitle(post.getTitle() != null ? post.getTitle() : oldPost.getTitle());
            oldPost.setContent(post.getContent() != null ? post.getContent() : oldPost.getContent());
            oldPost.setPhotoUrl(post.getPhotoUrl() != null ? post.getPhotoUrl() : oldPost.getPhotoUrl());
            postRepository.save(oldPost);
        }
        PostResponse response = mapPostToPostResponse(post);
        return new ApiResponse("Post", true, oldPost.getId(), "", Arrays.asList(response).size(), Arrays.asList(response));
    }

    @Override
    public ApiResponse likePost(UUID postId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post Not Found"));

        post.setLikes(post.getLikes() + 1);
        postRepository.save(post);

        PostResponse response = mapPostToPostResponse(post);
        response.setOwner(mapUserToUserResponse(user));

        return new ApiResponse("Post", true, post.getId(), "", Arrays.asList(response).size(), Arrays.asList(response));
    }

    @Override
    public ApiResponse dislikePost(UUID postId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post Not Found"));
        post.setDislikes(post.getDislikes() + 1);
        postRepository.save(post);

        PostResponse response = mapPostToPostResponse(post);
        response.setOwner(mapUserToUserResponse(user));

        return new ApiResponse("Post", true, post.getId(), "", Arrays.asList(response).size(), Arrays.asList(response));
    }

    @Override
    public ApiResponse getAllPosts() {
        List<Post> posts = postRepository.findAll();

        List<PostResponse> responses = posts.stream().map(post -> mapPostToPostResponse(post)).collect(Collectors.toList());


        return new ApiResponse("Post", true, null, "", responses.size(), responses);
    }

    @Override
    public ApiResponse uploadPostPhotoUrl(UUID postId, MultipartFile file, String username) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post Not Found"));

        String fileName = null;
        try {
            File tempFile = utils.converMultipartFileToFile(file);
            if (post != null) {
                if (post.getPhotoUrl() != null) {
                    amazonS3.deleteObject(bucketName, post.getPhotoUrl());
                }
                fileName = System.currentTimeMillis() +
                        "_" +
                        post.getId().toString() +
                        "_" +
                        file.getOriginalFilename().trim();
                amazonS3.putObject(
                        new PutObjectRequest(bucketName, fileName, tempFile));

                post.setPhotoUrl(fileName);
                postRepository.save(post);

            }
        } catch (Exception e) {
            System.out.println("Error saving file : " + e);
            throw new ResourceNotFoundException("AWS Error : While uploading post photo");
        }
        PostResponse response = mapPostToPostResponse(post);
        return new ApiResponse("Post", true, post.getId(), "", Arrays.asList(response).size(), Arrays.asList(response));
    }

    @Override
    public ApiResponse deletePost(UUID postId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post Not Found"));

        String message = "";
        if (post.getUser().getId().equals(user.getId())) {
            postRepository.delete(post);
            message = "Post Deleted Successfully";
        } else {
            throw new BadRequestException("You can only delete your own posts.");
        }

        return new ApiResponse("Post", true, null, message, 0, null);
    }

    private PostResponse mapPostToPostResponse(Post post) {
        UserResponse owner = mapUserToUserResponse(post.getUser());
        PostResponse response = modelMapper.map(post, PostResponse.class);
        response.setOwner(owner);
        response.setPhotoUrl(post.getPhotoUrl() != null ? bucketUri + post.getPhotoUrl() : bucketUri + "noimage.jpg");
        return response;
    }

    private UserResponse mapUserToUserResponse(User user){
        return modelMapper.map(user, UserResponse.class);
    }
}
