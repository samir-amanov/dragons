package com.mugloar.dragons;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.mugloar.dragons.runner.GameRunner;

@SpringBootApplication
public class DragonsOfMugloarApplication  {

	public static void main(String[] args) {
		SpringApplication.run(DragonsOfMugloarApplication .class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public CommandLineRunner gameStarter(GameRunner gameRunner) {
	    return args -> gameRunner.runGame();
	}
}
