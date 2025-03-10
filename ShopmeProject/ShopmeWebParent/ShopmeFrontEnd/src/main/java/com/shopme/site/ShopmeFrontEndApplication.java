package com.shopme.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.shopme.common.entities","com.shopme.site"})
public class ShopmeFrontEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopmeFrontEndApplication.class, args);
	}

}
