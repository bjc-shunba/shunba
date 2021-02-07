package com.baidu.shunba;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.baidu.shunba.core.persistence.service.SystemService;
import com.baidu.shunba.socket.service.ShunbaSocketService;

@EnableScheduling
@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class, FreeMarkerAutoConfiguration.class})
@EnableEurekaClient
@EnableJpaRepositories
public class ShunbaSocketServerApplication {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ShunbaSocketServerApplication.class);

	public static SystemService systemService;

	@Autowired
	public void setSystemService(SystemService systemService_) {
		systemService = systemService_;
	}

	public static void main(String[] args) {
		SpringApplication.run(ShunbaSocketServerApplication.class, args);

		logger.info("Baidu Shunba Socket Server is starting...");

		try {
			ShunbaSocketService.start(systemService);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		logger.info("Baidu Shunba Socket Server test cache is complete.");

	}

}
