package com.iskandarov.sbservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync // Required to make @Async work
@EnableScheduling //Enable scheduled methods
public class SbserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbserviceApplication.class, args);
	}

}
