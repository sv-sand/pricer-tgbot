package ru.svsand.pricer.tgbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Boot application entry point for the Pricer Telegram bot microservice.
 *
 * @author sand <sve.snd@gmail.com>
 * @since 04.11.2025
 */
@SpringBootApplication
@EnableScheduling
public class Application {

	/**
	 * Starts the Spring Boot application.
	 *
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
