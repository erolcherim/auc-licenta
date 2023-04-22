package com.unibuc.auclicenta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class AucLicentaApplication {
	public static void main(String[] args) {
		SpringApplication.run(AucLicentaApplication.class, args);
	}

}
