package com.nl.sprinterbe.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nl.sprinterbe.dto.StartingDataDto;
import com.nl.sprinterbe.dto.StartingFormDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class StartingFormService {
    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public StartingFormService(RestTemplateBuilder builder , ObjectMapper objectMapper) {
        this.restTemplate = builder.build();
        this.objectMapper = objectMapper;
    }

    public StartingDataDto generateProjectPlan(StartingFormDto requestDTO) {
        String url = "https://api.openai.com/v1/chat/completions";
        System.out.println("apiKey = " + apiKey);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "gpt-4o");
        payload.put("messages", buildPrompt(requestDTO));
        payload.put("response_format", Map.of("type", "json_object"));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println("response = " + response);
        return parseGPTResponse(response.getBody());
    }

    private Object buildPrompt(StartingFormDto requestDTO) {
        String systemPrompt = "너는 trello나 jira같은 회사의 프로젝트를 관리하고 팀원을 관리하는 AI야.  사용자가 Sprint(애자일 프로그래밍) 관련 데이터를 입력하면, JSON 형식으로 다음 정보를 반환해야 해. \n" +
                "일단 사용자의 질문 리스트를 보여줄 게\n" +
                "{-프로젝트 이름을 정해주세요. \n" +
                "-프로젝트의 목표는 무엇인가요? \n" +
                "-프로젝트의 예상 기간은 어느 정도인가요? \n" +
                "-스프린트 주기를 설정해주세요. \n" +
                "-팀원은 몇 명인가요?\n" +
                "-프로젝트에서 필수적으로 구현해야 하는 기능을 나열해주세요. (로그인/회원가입, 게시판, 채팅 등)}\n" +
                "\n" +
                "해당 질문들에 대한 답변이 json 형식으로 너한테 전달이 될거야. 그러면 너는 해당 데이터를 보고 \n" +
                "{- 프로젝트명 (project_name)\n" +
                "- 스프린트 개수 (sprint_count)\n" +
                "- 스프린트 기간 (sprint_duration)(주 단위가 아닌 일 단위로 리턴)\n" +
                "- 백로그 항목 (backlog: [{sprint_number,title, weight}])}를 json형식으로 리턴하면 돼.\n" +
                "(너가 판단하기에 좋은 backlog가 있다면 추가해도 되고 weight는 업무 난이도기 때문에 너가 판단해서 넣으면 돼, sprint_number는 해당 백로그가 몇번쨰 스프린터에 속해있는지를 나타내는 거야.)\n" +
                "\n" +
                "반드시 JSON 형식으로 출력해야 해. 예시는 다음과 같아:\n" +
                "{\n" +
                "  \"project\": {\n" +
                "    \"project_name\": \"업무 시스템\"\n" +
                "  },\n" +
                "  \"sprints\": {\n" +
                "    \"sprint_count\": \"4\",\n" +
                "    \"sprint_duration\": \"14\"\n" +
                "  },\n" +
                "  \"backlog\": [\n" +
                "    {\n" +
                "      “sprint_number”: “3”\n" +
                "      \"title\": \"JWT 기반 로그인/회원가입 구현\",\n" +
                "      \"weight\": \"3\"\n" +
                "    },\n" +
                "    {\n" +
                "     “sprintNumber”: “2”\n" +
                "      \"title\": \"GPT API 활용 자동 응답 시스템\",\n" +
                "      \"weight\": \"1\"\n" +
                "    },\n" +
                "    {\n" +
                "     “sprintNumber”: “1”\n" +
                "      \"title\": \"React UI 구현\",\n" +
                "      \"weight\": \"2\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n.";
        return new Object[]{
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", generateUserPrompt(requestDTO))
        };
    }

    private String generateUserPrompt(StartingFormDto requestDTO) {
        return String.format("""
            {
                "project_name": "%s",
                "project_goal": "%s",
                "estimated_duration": "%s",
                "sprint_cycle": "%s",
                "team_members": %d,
                "essential_features": %s
            }
            """,
                requestDTO.getProjectName(),
                requestDTO.getProjectGoal(),
                requestDTO.getEstimatedDuration(),
                requestDTO.getSprintCycle(),
                requestDTO.getTeamMembers(),
                requestDTO.getEssentialFeatures()
        );
    }

    public StartingDataDto parseGPTResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode contentNode = root.path("choices").get(0).path("message").path("content");
            return objectMapper.readValue(contentNode.asText(), StartingDataDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse GPT response", e);
        }
    }
}