package com.hereforbeer;

import com.hereforbeer.offer_gap.OfferGapRepository;
import com.hereforbeer.offer_gap.OfferService;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuctionHelperApplication {


	public static void main(String[] args) {
		SpringApplication.run(AuctionHelperApplication.class, args);
	}
}
