package com.tyagi.demoapplication.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tyagi.demoapplication.enums.RoleEnum;

import java.sql.Timestamp;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@Table(name = "role")
public class Role {

  @Id
  @GeneratedValue
  private UUID id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private RoleEnum name;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Column(updatable = false)
  @CreationTimestamp
  private Timestamp createdDate;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @UpdateTimestamp
  private Timestamp updatedDate;
}
