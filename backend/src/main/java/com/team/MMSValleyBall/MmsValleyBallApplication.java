package com.team.MMSValleyBall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MmsValleyBallApplication {
	public static void main(String[] args) {
		SpringApplication.run(MmsValleyBallApplication.class, args);
	}
}
