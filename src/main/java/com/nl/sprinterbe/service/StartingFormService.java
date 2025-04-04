package com.nl.sprinterbe.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nl.sprinterbe.dto.StartingDataDto;
import com.nl.sprinterbe.dto.StartingFormDto;
import com.nl.sprinterbe.global.exception.form.FileReadException;
import com.nl.sprinterbe.global.exception.form.JsonParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class StartingFormService {
    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.model}")
    private String model;

    @Value("${system.txt.path}")
    private String systemPromptPath;

    @Value("${user.txt.path}")
    private String userPromptPath;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;


    public StartingFormService(RestTemplateBuilder builder , ObjectMapper objectMapper, ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.restTemplate = builder.build();
        this.objectMapper = objectMapper;
    }

    public StartingDataDto generateProjectPlan(StartingFormDto requestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", model);
        payload.put("messages", buildPrompt(requestDTO));
        payload.put("response_format", Map.of("type", "json_object"));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        return parseGPTResponse(response.getBody());
//        StartingDataDto dummyStartingDataDto = new StartingDataDto(
//                new StartingDataDto.ProjectInfo(
//                        "카카오톡"
//                ),
//                new StartingDataDto.SprintInfo(
//                        3,
//                        30
//                ),
//                List.of(
//                        new StartingDataDto.BacklogItem(
//                                1,
//                                "친구 등록 기능 구현",
//                                3L
//                        ),
//                        new StartingDataDto.BacklogItem(
//                                1,
//                                "친구 삭제 기능 구현",
//                                1L
//                        ),
//                        new StartingDataDto.BacklogItem(
//                                1,
//                                "친구 조회 기능 구현",
//                                2L
//                        ),
//                        new StartingDataDto.BacklogItem(
//                                2,
//                                "기본 채팅 기능 구현",
//                                3L
//                        ),
//                        new StartingDataDto.BacklogItem(
//                                2,
//                                "단체 채팅 기능 구현",
//                                3L
//                        ),
//                        new StartingDataDto.BacklogItem(
//                                3,
//                                "사진 및 동영상 보내기 기능 구현",
//                                2L
//                        ),
//                        new StartingDataDto.BacklogItem(
//                                3,
//                                "음성 통화 기능",
//                                2L
//                        ),
//                        new StartingDataDto.BacklogItem(
//                                3,
//                                "최종 테스트",
//                                2L
//                        )
//                )
//        );
//        return dummyStartingDataDto; // 더미데이터 주입

    }

    private Object buildPrompt(StartingFormDto startingFormDto) {
        String systemPrompt = readFile(this.systemPromptPath);
        String userPrompt = generateUserPrompt(startingFormDto);
        return new Object[]{
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt)
        };
    }

    private String generateUserPrompt(StartingFormDto startingFormDto) {
        String userPromptTemplate = readFile(userPromptPath);
        return String.format(userPromptTemplate,
                startingFormDto.getProjectName(),
                startingFormDto.getProjectGoal(),
                startingFormDto.getEstimatedDuration(),
                startingFormDto.getSprintCycle(),
                startingFormDto.getTeamMembers(),
                startingFormDto.getEssentialFeatures()
        );
    }

    private String readFile(String filePath) {
        try {
            Resource resource = resourceLoader.getResource(filePath);
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new FileReadException();
        }
    }

    public StartingDataDto parseGPTResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode contentNode = root.path("choices").get(0).path("message").path("content");
            return objectMapper.readValue(contentNode.asText(), StartingDataDto.class);
        } catch (Exception e) {
            throw new JsonParseException();
        }
    }
}