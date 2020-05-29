package com.hh.smscat;

import org.apache.log4j.PropertyConfigurator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("com.hh.smscat.mapper")
@ServletComponentScan
public class SMSCatApplication {

	public static void main(String[] args) {
		SpringApplication.run(SMSCatApplication.class, args);
	}
}
