package com.nl.sprinterbe;

import com.nl.sprinterbe.global.config.SecurityConfig;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class SprinterbeApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		settingEnv(dotenv);
		SpringApplication.run(SprinterbeApplication.class, args);
	}

	private static void settingEnv(Dotenv dotenv) {
		// MySQL 설정
//		System.setProperty("MYSQL_URL", dotenv.get("MYSQL_URL"));
//		System.setProperty("MYSQL_USERNAME", dotenv.get("MYSQL_USERNAME"));
//		System.setProperty("MYSQL_PASSWORD", dotenv.get("MYSQL_PASSWORD"));
//
//		// OAuth2 설정
//		System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
//		System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));
//		System.setProperty("GOOGLE_REDIRECT_URI", dotenv.get("GOOGLE_REDIRECT_URI"));

		// JWT 설정
		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));

//		// OpenAI API 설정
//		System.setProperty("OPENAI_API_KEY", dotenv.get("OPENAI_API_KEY"));
//		System.setProperty("OPENAI_API_URL", dotenv.get("OPENAI_API_URL"));
//		System.setProperty("OPENAI_API_MODEL", dotenv.get("OPENAI_API_MODEL"));
	}
}
