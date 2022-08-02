package com.subscription.server;

import com.subscription.server.DTO.ServerDTO;
import com.subscription.server.model.ServerDAO;
import com.subscription.server.modelMapper.ModelMapper;
import com.subscription.server.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ServerApplication {
	@Autowired
	ServerRepository serverRepository;
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
