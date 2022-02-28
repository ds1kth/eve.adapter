package com.chargev.eve.adapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AdapterApplication {

	public static void main(String[] args) {
		//SpringApplication.run(AdapterApplication.class, args);
		ConfigurableApplicationContext context = SpringApplication.run(AdapterApplication.class, args);
		AppContext.setContext(context);
	}
}
