package com.tyagi.demoapplication.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tyagi.demoapplication.model.Role;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

  private UUID id;
  private String name;
  private String username;
  private Boolean isActive;
  private List<Role> roles = new ArrayList<>();
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Timestamp createdDate;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Timestamp updatedDate;

}
