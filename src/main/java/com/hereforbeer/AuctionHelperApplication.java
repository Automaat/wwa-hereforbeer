package com.hereforbeer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AuctionHelperApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuctionHelperApplication.class, args);
	}

}
