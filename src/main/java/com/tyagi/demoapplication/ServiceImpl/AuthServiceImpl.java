package com.tyagi.demoapplication.ServiceImpl;

import com.tyagi.demoapplication.Exception.ResourceNotFoundException;
import com.tyagi.demoapplication.Service.AuthService;
import com.tyagi.demoapplication.enums.RoleEnum;
import com.tyagi.demoapplication.model.User;
import com.tyagi.demoapplication.payload.ApiResponse;
import com.tyagi.demoapplication.payload.JwtAuthRequest;
import com.tyagi.demoapplication.payload.UserResponse;
import com.tyagi.demoapplication.repository.RoleRepository;
import com.tyagi.demoapplication.repository.UserRepository;
import java.util.Arrays;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private ModelMapper modelMapper;

  /* REGISTER */
  @Override
  public ApiResponse register(JwtAuthRequest request) {
    Boolean existingUser = userRepository.existsByUsername(
      request.getUsername()
    );
    if (existingUser) throw new ResourceNotFoundException("User Exists");

    User user = modelMapper.map(request, User.class);

    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    user.setIsActive(true);
    user
      .getRoles()
      .add(
        roleRepository
          .findByName(RoleEnum.ROLES_USER)
          .orElseThrow(() -> new ResourceNotFoundException("Role Not Found"))
      );

    userRepository.save(user);
    UserResponse response = modelMapper.map(user, UserResponse.class);
    return new ApiResponse(
      "User",
      true,
      user.getId(),
      null,
      1,
      Arrays.asList(response)
    );
  }
}
