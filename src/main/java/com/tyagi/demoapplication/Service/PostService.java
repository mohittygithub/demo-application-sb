package com.tyagi.demoapplication.Service;

import com.tyagi.demoapplication.model.Post;
import com.tyagi.demoapplication.payload.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface PostService {
    ApiResponse testPostService();

    ApiResponse createPost(Post post, String username);

    ApiResponse updatePost(UUID postId, Post post, String username);

    ApiResponse likePost(UUID postId, String username);

    ApiResponse dislikePost(UUID postId, String username);

    ApiResponse getAllPosts();

    ApiResponse uploadPostPhotoUrl(UUID postId, MultipartFile file, String username);

    ApiResponse deletePost(UUID postId, String username);


}
