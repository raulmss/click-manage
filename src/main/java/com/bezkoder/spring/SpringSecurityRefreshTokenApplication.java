package com.bezkoder.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.bezkoder.spring.security",
		"com.bezkoder.spring.inventory"
})
public class SpringSecurityRefreshTokenApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityRefreshTokenApplication.class, args);
	}

}
