package com.tyagi.demoapplication.Service;

import com.tyagi.demoapplication.payload.ApiResponse;
import com.tyagi.demoapplication.payload.UpdateUserRequest;
import java.util.UUID;

public interface UserService {
  ApiResponse update(
    UUID userId,
    UpdateUserRequest updateUserRequest,
    String username
  );

  ApiResponse delete(UUID userId, String username);
}
