package com.nl.sprinterbe;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SprinterbeApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		settingEnv(dotenv);
		SpringApplication.run(SprinterbeApplication.class, args);
	}

	private static void settingEnv(Dotenv dotenv) {
		// 환경 변수를 시스템 속성에 설정
		System.setProperty("KAKAO_CLIENT_ID", dotenv.get("KAKAO_CLIENT_ID"));
		System.setProperty("KAKAO_CLIENT_SECRET", dotenv.get("KAKAO_CLIENT_SECRET"));
		System.setProperty("KAKAO_REDIRECT_URI", dotenv.get("KAKAO_REDIRECT_URI"));

		System.setProperty("NAVER_CLIENT_ID", dotenv.get("NAVER_CLIENT_ID"));
		System.setProperty("NAVER_CLIENT_SECRET", dotenv.get("NAVER_CLIENT_SECRET"));
		System.setProperty("NAVER_REDIRECT_URI", dotenv.get("NAVER_REDIRECT_URI"));

		System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
		System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));
		System.setProperty("GOOGLE_REDIRECT_URI", dotenv.get("GOOGLE_REDIRECT_URI"));

		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));


		System.setProperty("DATABASE_NAME", dotenv.get("DATABASE_NAME"));
		System.setProperty("DATABASE_USERNAME", dotenv.get("DATABASE_USERNAME"));
		System.setProperty("DATABASE_PASSWORD", dotenv.get("DATABASE_PASSWORD"));

		System.setProperty("LOCAL_DDL_TYPE", dotenv.get("LOCAL_DDL_TYPE"));

		System.setProperty("API_KEY", dotenv.get("API_KEY"));
	}
}
