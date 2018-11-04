package com.systemDemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@MapperScan("com.systemDemo.mapper")
@EnableEurekaClient
@SpringBootApplication
public class SystemDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SystemDemoApplication.class, args);
	}
}
