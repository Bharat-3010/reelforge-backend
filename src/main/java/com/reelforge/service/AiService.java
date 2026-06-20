package com.reelforge.service;

import com.reelforge.dto.AiRequest;
import com.reelforge.entity.GeneratedContent;
import com.reelforge.repository.GeneratedContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    private final RestTemplate restTemplate;

    private final GeneratedContentRepository repository;


    @Value("${openrouter.api.key}")
    private String apiKey;

    public String generateContent(AiRequest request) {

        try {

            String prompt = """
                Generate a viral short-form reel script.

                Topic: %s
                Niche: %s
                Platform: %s
                Tone: %s

                Give response in this format:

                Hook:
                Script:
                CTA:
                Hashtags:
                """
                    .formatted(
                            request.getTopic(),
                            request.getNiche(),
                            request.getPlatform(),
                            request.getTone()
                    );

            String url = "https://openrouter.ai/api/v1/chat/completions";

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            headers.setBearerAuth(apiKey);

            Map<String, Object> body = Map.of(

                    "model", "openai/gpt-3.5-turbo",

                    "messages", new Object[]{

                            Map.of(
                                    "role", "user",
                                    "content", prompt
                            )
                    }
            );

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(body, headers);

            ResponseEntity<Map> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            Map.class
                    );

            java.util.List choices =
                    (java.util.List) response.getBody().get("choices");

            Map choice =
                    (Map) choices.get(0);

            Map message =
                    (Map) choice.get("message");

            String aiResponse =
                    message.get("content").toString();

            Authentication authentication =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            String userEmail =
                    authentication.getName();

            GeneratedContent content =
                    GeneratedContent.builder()
                            .topic(request.getTopic())
                            .niche(request.getNiche())
                            .platform(request.getPlatform())
                            .tone(request.getTone())
                            .userEmail(userEmail)
                            .generatedText(aiResponse)
                            .build();


            repository.save(content);

            return aiResponse;

        } catch (Exception e) {

            e.printStackTrace();

            return "AI generation failed.";
        }
    }
    public java.util.List<GeneratedContent> getHistory() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String userEmail =
                authentication.getName();

        return repository.findByUserEmail(userEmail);
    }
}
