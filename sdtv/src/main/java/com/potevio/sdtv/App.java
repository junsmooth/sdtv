package com.potevio.sdtv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * main entry
 * 
 * @author wimms
 *
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
public class App {
	private static ConfigurableApplicationContext ctx;

	public static void main(String[] args) {
		ctx = SpringApplication.run(App.class, args);
	}

	public static ConfigurableApplicationContext getContext() {
		return ctx;
	}
}
