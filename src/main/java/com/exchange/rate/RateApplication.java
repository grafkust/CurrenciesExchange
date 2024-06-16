package com.exchange.rate;

import com.exchange.rate.util.ErrorMessageBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RateApplication {

	public static void main(String[] args) {
		SpringApplication.run(RateApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper (){
		return new ModelMapper();
	}

	@Bean
	public ErrorMessageBuilder errorMessageBuilder(){
		return new ErrorMessageBuilder();
	}


}
