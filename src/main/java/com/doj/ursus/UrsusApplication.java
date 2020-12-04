package com.doj.ursus;

import com.doj.ursus.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UrsusApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrsusApplication.class, args);
	}

}
