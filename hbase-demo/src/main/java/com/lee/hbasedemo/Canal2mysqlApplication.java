package com.lee.hbasedemo;

import com.lee.hbasedemo.test.TransForm;
import com.lee.hbasedemo.test.WebLogDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@SpringBootApplication
@EnableScheduling
//@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
//@ImportResource("classpath:applicationContext.xml")

public class Canal2mysqlApplication {
	public static void main(String[] args) {

		SpringApplication.run(Canal2mysqlApplication.class, args);
		TransForm transForm = (TransForm) TransForm.getBean("TransForm");
		transForm.transForm();

	}
}
