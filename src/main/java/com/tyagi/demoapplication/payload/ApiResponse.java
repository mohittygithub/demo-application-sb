package com.tyagi.demoapplication.payload;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

  private String objectName;
  private Boolean success;
  private UUID recordId;
  private String message;
  private Integer recordsCount;
  private List<?> records = new ArrayList<>();
}
