package com.blackdeath.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.blackdeath.springboot.app.models.service.IUploadImageService;

@SpringBootApplication
public class SpringBootDataJpaApplication implements CommandLineRunner {

	@Autowired
	private IUploadImageService uploadImageService;

	@Override
	public void run(String... args) throws Exception {
		uploadImageService.deleteUploadsDirectory();
		uploadImageService.createUploadsDirectory();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

}
