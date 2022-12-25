package com.tyagi.demoapplication.controller;

import com.tyagi.demoapplication.Service.PostService;
import com.tyagi.demoapplication.model.Post;
import com.tyagi.demoapplication.payload.ApiResponse;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;

  @Autowired
  public PostController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping("/test")
  public ResponseEntity<ApiResponse> testPostService() {
    log.info("=====PostController Test =====");
    return new ResponseEntity<>(
      postService.testPostService(),
      HttpStatus.CREATED
    );
  }

  @GetMapping("/all")
  public ResponseEntity<ApiResponse> getAllPosts() {
    return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.CREATED);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> createPost(@RequestBody Post post) {
    log.info("=====Create=====");
    String username = SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getName();
    return new ResponseEntity<>(
      postService.createPost(post, username),
      HttpStatus.CREATED
    );
  }

  @GetMapping("/{postId}")
  public ResponseEntity<ApiResponse> updatePost(
    @PathVariable UUID postId,
    @RequestBody Post post
  ) {
    log.info("=====Update Post=====");
    return new ResponseEntity<>(
      postService.updatePost(
        postId,
        post,
        SecurityContextHolder.getContext().getAuthentication().getName()
      ),
      HttpStatus.CREATED
    );
  }

  @GetMapping("/like/{postId}")
  public ResponseEntity<ApiResponse> likePost(@PathVariable UUID postId) {
    return new ResponseEntity<>(
      postService.likePost(
        postId,
        SecurityContextHolder.getContext().getAuthentication().getName()
      ),
      HttpStatus.CREATED
    );
  }

  @GetMapping("/dislike/{postId}")
  public ResponseEntity<ApiResponse> dislikePost(@PathVariable UUID postId) {
    return new ResponseEntity<>(
      postService.dislikePost(
        postId,
        SecurityContextHolder.getContext().getAuthentication().getName()
      ),
      HttpStatus.CREATED
    );
  }

  @PostMapping(
    path = "/upload/photo/{postId}",
    consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
  )
  public ResponseEntity<ApiResponse> uploadPostPhotoUrl(
    @PathVariable UUID postId,
    @RequestPart("file") MultipartFile file
  ) {
    String username = SecurityContextHolder
      .getContext()
      .getAuthentication()
      .getName();
    return new ResponseEntity<>(
      postService.uploadPostPhotoUrl(postId, file, username),
      HttpStatus.CREATED
    );
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<ApiResponse> deletePost(@PathVariable UUID postId) {
    return new ResponseEntity<>(
      postService.deletePost(
        postId,
        SecurityContextHolder.getContext().getAuthentication().getName()
      ),
      HttpStatus.CREATED
    );
  }
}
