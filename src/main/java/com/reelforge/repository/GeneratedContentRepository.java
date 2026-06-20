package com.reelforge.repository;

import com.reelforge.entity.GeneratedContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeneratedContentRepository
        extends JpaRepository<GeneratedContent, Long> {

    List<GeneratedContent> findByUserEmail(String userEmail);
}