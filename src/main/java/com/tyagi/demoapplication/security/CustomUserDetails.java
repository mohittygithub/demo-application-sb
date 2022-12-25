package com.tyagi.demoapplication.security;

import com.tyagi.demoapplication.model.Role;
import com.tyagi.demoapplication.model.User;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

  private static final long serialVersionUID = 1L;

  private String username;
  private String password;
  private Boolean isActive;
  private List<Role> roles;
  private List<GrantedAuthority> authorities;

  public CustomUserDetails(User user) {
    this.username = user.getUsername();
    this.password = user.getPassword();
    this.isActive = user.getIsActive();
    this.roles = user.getRoles();
    this.authorities =
      user
        .getRoles()
        .stream()
        .map(role -> new SimpleGrantedAuthority(role.getName().toString()))
        .collect(Collectors.toList());
    // this.authorities =
    //   Arrays
    //     .stream(user.getRole().getName().split(","))
    //     .map(SimpleGrantedAuthority::new)
    //     .collect(Collectors.toList());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return isActive;
  }

  public List<Role> getRoles() {
    return roles;
  }
}
