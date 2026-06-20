package com.reelforge.controller;

import com.reelforge.dto.AiRequest;
import com.reelforge.repository.GeneratedContentRepository;
import com.reelforge.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AiController {

    private final AiService aiService;

    private final GeneratedContentRepository repository;

    @PostMapping("/generate")
    public String generate(
            @RequestBody AiRequest request
    ) {

        return aiService.generateContent(request);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getHistory() {

        return ResponseEntity.ok(
                aiService.getHistory()
        );
    }

    @DeleteMapping("/{id}")
    public String deleteContent(
            @PathVariable Long id
    ) {

        repository.deleteById(id);

        return "Deleted Successfully";
    }
}