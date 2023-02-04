package com.youngpopeugene.vkservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@EnableCaching
@SpringBootApplication
public class VkServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(VkServiceApplication.class, args);
	}

}
