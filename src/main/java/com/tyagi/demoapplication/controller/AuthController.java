package com.tyagi.demoapplication.controller;

import com.tyagi.demoapplication.Exception.ResourceNotFoundException;
import com.tyagi.demoapplication.Service.AuthService;
import com.tyagi.demoapplication.payload.ApiResponse;
import com.tyagi.demoapplication.payload.JwtAuthRequest;
import com.tyagi.demoapplication.payload.JwtAuthResponse;
import com.tyagi.demoapplication.security.CustomUserDetailsService;
import com.tyagi.demoapplication.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final CustomUserDetailsService customUserDetailsService;
  private final JwtUtils jwtUtils;
  private final AuthService authService;

  @Autowired
  public AuthController(
    AuthenticationManager authenticationManager,
    CustomUserDetailsService customUserDetailsService,
    JwtUtils jwtUtils,
    AuthService authService
  ) {
    this.authenticationManager = authenticationManager;
    this.customUserDetailsService = customUserDetailsService;
    this.jwtUtils = jwtUtils;
    this.authService = authService;
  }

  /* AUTHENTICATE */
  @PostMapping("/authenticate")
  @Operation(summary = "Authenticate API")
  public JwtAuthResponse createAuthenticationToken(
    @RequestBody JwtAuthRequest request
  ) throws Exception {
    log.info("=====Authenticate=====");
    try {
      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
          request.getUsername(),
          request.getPassword()
        )
      );
    } catch (Exception ex) {
      throw new ResourceNotFoundException("Incorrect Username/Password");
    }
    final UserDetails userDetails = customUserDetailsService.loadUserByUsername(
      request.getUsername()
    );
    final String jwt = jwtUtils.generateToken(userDetails);

    return new JwtAuthResponse(jwt, userDetails.getUsername());
  }

  /* REGISTER */
  @PostMapping("/register")
  public ResponseEntity<ApiResponse> register(
    @RequestBody JwtAuthRequest request
  ) {
    log.info("=====Register=====");

    return new ResponseEntity<>(
      authService.register(request),
      HttpStatus.CREATED
    );
  }
}
