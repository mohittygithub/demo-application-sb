package com.tyagi.demoapplication.Service;

import com.tyagi.demoapplication.payload.ApiResponse;
import com.tyagi.demoapplication.payload.JwtAuthRequest;

public interface AuthService {
  ApiResponse register(JwtAuthRequest request);
}
