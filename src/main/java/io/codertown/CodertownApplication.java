package io.codertown;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(modifyOnCreate = false) // 저장 시점에 created데이터만 입력 (lastModified는 null로 처리)
@SpringBootApplication
public class CodertownApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodertownApplication.class, args);
	}

}
