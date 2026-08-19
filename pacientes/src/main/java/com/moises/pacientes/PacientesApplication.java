package com.moises.pacientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.moises.pacientes", "com.moises.commons"})
public class PacientesApplication {

	public static void main(String[] args) {
		SpringApplication.run(PacientesApplication.class, args);
	}

}
