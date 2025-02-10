package com.nl.sprinterbe.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nl.sprinterbe.common.ResponseStatus;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static com.nl.sprinterbe.dto.ErrorDto.mapper;


@Getter
public class ResponseDto<T> {
    private int status;
    private String code;
    private String message;
    private T data;

    public ResponseDto(HttpStatus httpStatus, ResponseStatus status, T data) {
        this.status = httpStatus.value();
        this.message = status.getMessage();
        this.code = status.getCode();
        this.data = data;
    }

    public static <T> ResponseDto<T> success(ResponseStatus status,T data){
        return new ResponseDto<>(HttpStatus.OK, status,data);
    }

    public static ResponseDto<?> success(ResponseStatus status){
        return new ResponseDto<>(HttpStatus.OK,status, null);
    }


    //필터단: data 필드 없는 Json
    public static void settingResponse(HttpServletResponse response, HttpStatus httpStatus, ResponseStatus status , String accessToken , String refreshToken) throws IOException {
        // 응답 헤더 및 쿠키 설정
        if (accessToken != null) {
            response.addHeader("Authorization", "Bearer " + accessToken);
        }
        response.setHeader("Set-Cookie","Refresh="+refreshToken+"; Path=/; Max-Age=259200; HttpOnly");
        ResponseDto<?> success = ResponseDto.success(status);
        String json = mapper.writeValueAsString(success);
        response.setStatus(httpStatus.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(json);
    }

    //필터단: data 필드 있는 Json
    public static <T> void settingResponse(HttpServletResponse response, ResponseStatus status,T data) throws IOException {
        ResponseDto<T> success = ResponseDto.success(status, data);
        String json = mapper.writeValueAsString(success);
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(json);
    }

    //스프링: data 필드 없는 Json
    public static ResponseEntity<ResponseDto<?>> settingResponse(HttpStatus httpStatus , ResponseStatus status){
        ResponseDto<?> dto = success(status);
        return ResponseEntity.status(httpStatus).body(dto);
    }

    //스프링: data 필드 없는 Json
    public static <T> ResponseEntity<ResponseDto<T>> settingResponse(HttpStatus httpStatus , ResponseStatus status,T data){
        ResponseDto<T> dto = success(status,data);
        return ResponseEntity.status(httpStatus).body(dto);
    }

}
