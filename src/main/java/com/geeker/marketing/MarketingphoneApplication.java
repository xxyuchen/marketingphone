package com.geeker.marketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class MarketingphoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketingphoneApplication.class, args);
	}
	@RequestMapping("/hello")
	public String hello(){
		return "this is Marketing mobile phone!";
	}
}
