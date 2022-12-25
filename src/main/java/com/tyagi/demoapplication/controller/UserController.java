package com.tyagi.demoapplication.controller;

import com.tyagi.demoapplication.Service.UserService;
import com.tyagi.demoapplication.payload.ApiResponse;
import com.tyagi.demoapplication.payload.UpdateUserRequest;
import java.security.Principal;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PutMapping("/{userId}")
  public ResponseEntity<ApiResponse> update(
    @PathVariable UUID userId,
    @RequestBody UpdateUserRequest updateUserRequest,
    Principal principal
  ) {
    return new ResponseEntity<ApiResponse>(
      userService.update(userId, updateUserRequest, principal.getName()),
      HttpStatus.OK
    );
  }

  @DeleteMapping("{userId}")
  public ResponseEntity<ApiResponse> delete(
    @PathVariable UUID userId,
    Principal principal
  ) {
    return new ResponseEntity<>(
      userService.delete(userId, principal.getName()),
      HttpStatus.OK
    );
  }
}
