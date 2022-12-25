package com.tyagi.demoapplication.security;

import com.tyagi.demoapplication.model.User;
import com.tyagi.demoapplication.repository.UserRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public CustomUserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    log.info("======CustomUserDetailsService======");

    Optional<User> users = userRepository.findByUsername(username);
    User user = users.get();
    if (user == null) {
      throw new UsernameNotFoundException(
        "User not found with username : " + username
      );
    }
    return new CustomUserDetails(user);
  }
}
