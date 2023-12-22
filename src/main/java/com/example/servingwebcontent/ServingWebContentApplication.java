package com.example.servingwebcontent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServingWebContentApplication {

	public static void main(String[] args) {

		SpringApplication.run(ServingWebContentApplication.class, args);
    }


}


