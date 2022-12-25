package com.tyagi.demoapplication.repository;

import com.tyagi.demoapplication.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
}
