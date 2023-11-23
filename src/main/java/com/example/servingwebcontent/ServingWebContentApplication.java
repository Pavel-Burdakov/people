package com.example.servingwebcontent;
import com.example.servingwebcontent.models.Person;
import com.example.servingwebcontent.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;


import java.util.Arrays;

@SpringBootApplication
public class ServingWebContentApplication {

	public static void main(String[] args) {

		SpringApplication.run(ServingWebContentApplication.class, args);
    }


}


