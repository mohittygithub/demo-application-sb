package com.tyagi.demoapplication.ServiceImpl;

import com.tyagi.demoapplication.Exception.InvalidJwtTokenException;
import com.tyagi.demoapplication.Exception.ResourceNotFoundException;
import com.tyagi.demoapplication.Service.UserService;
import com.tyagi.demoapplication.model.User;
import com.tyagi.demoapplication.payload.ApiResponse;
import com.tyagi.demoapplication.payload.UpdateUserRequest;
import com.tyagi.demoapplication.payload.UserResponse;
import com.tyagi.demoapplication.repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public ApiResponse update(
    UUID userId,
    UpdateUserRequest updateUserRequest,
    String username
  ) {
    User user = userRepository
      .findById(userId)
      .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

    if (
      !username.equals(user.getUsername())
    ) throw new InvalidJwtTokenException("Unauthorized Access");

    user.setName(
      updateUserRequest.getName() != null
        ? updateUserRequest.getName()
        : user.getName()
    );

    userRepository.save(user);
    UserResponse response = modelMapper.map(user, UserResponse.class);
    return new ApiResponse(
      "User",
      true,
      userId,
      "Updated Successfully",
      1,
      Arrays.asList(response)
    );
  }

  @Override
  public ApiResponse delete(UUID userId, String username) {
    User user = userRepository
      .findById(userId)
      .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

    if (
      !username.equals(user.getUsername())
    ) throw new InvalidJwtTokenException("Unauthorized Access");

    user.setIsActive(false);

    return new ApiResponse(
      "User",
      true,
      null,
      "Deleted Successfully",
      0,
      new ArrayList<>()
    );
  }
}
