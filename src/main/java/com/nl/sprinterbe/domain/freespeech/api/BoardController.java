package com.nl.sprinterbe.domain.freespeech.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardController {
    // 게시글 리스트 조회
    @GetMapping()
    public ResponseEntity<List<>>

    // 단건 삭제
    @DeleteMapping()
    public ResponseEntity<>

    // 등록
    @PostMapping
    public ResponseEntity<Void>
}
