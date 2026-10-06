package com.marcos.msrelatorio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsRelatorioApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsRelatorioApplication.class, args);
	}

}
