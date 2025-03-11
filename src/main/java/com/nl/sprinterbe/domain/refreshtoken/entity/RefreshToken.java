package com.nl.sprinterbe.domain.refreshtoken.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//RefreshToken이 발급되는 경우는
// 1) 다시 로그인 할때
// 2) Access 토큰으로 새로 발급할때
@Entity
@Getter
@Setter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_id")
    private Long id;

    //Local이면 DB Id , OAuth면 providerId가 이곳에 저장
    private String userId;

    //userId로 refresh 필드값 읽어와서 받은 RefreshToken과 비교
    private String refresh;

    private Boolean expired;
}
