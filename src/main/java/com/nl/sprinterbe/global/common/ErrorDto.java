package com.nl.sprinterbe.global.common;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nl.sprinterbe.global.common.code.ResponseStatus;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;


@Getter
public class ErrorDto {
    //상태코드
    private int status;
    //에러 유형
    private String error;
    //설명
    private String message;
    //내부 에러코드
    private String code;
    public static final ObjectMapper mapper = new ObjectMapper();
    public ErrorDto(HttpStatus httpStatus, ResponseStatus status) {
        this.status = httpStatus.value();
        this.error = httpStatus.name();
        this.message = status.getMessage();
        this.code = status.getCode();
    }

    //필터단(시큐리티)에서 JSON 처리
    public static void settingResponse(HttpServletResponse response, HttpStatus httpStatus , ResponseStatus status) throws IOException {
        ErrorDto dto = new ErrorDto(httpStatus, status);
        // 응답을 JSON으로 변환하여 보내기
        String json = mapper.writeValueAsString(dto);
        // 응답을 설정하고, JSON 형식으로 반환
        response.setStatus(httpStatus.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    //스프링에서 JSON 처리
    public static ResponseEntity<ErrorDto> settingResponse(HttpStatus httpStatus , ResponseStatus status){
        ErrorDto dto = new ErrorDto(httpStatus, status);
        return ResponseEntity.status(httpStatus).body(dto);
    }




    //여기서 에러 경우 하나씩... static 메서드로 작성

}
