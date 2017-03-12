package com.nomad.study.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by nomad on 2017. 3. 11..
 */
@SpringBootApplication
public class NomadRemoteService {

	@RestController
	public static class RemoteServiceController {

		@GetMapping("/service1")
		public String NomadService1(String req) throws InterruptedException {

			Thread.sleep(2000);
			return "NomadService :  " + req;
		}

		@GetMapping("/service2")
		public String NomadService2(String req) throws InterruptedException {

			Thread.sleep(2000);
			return "NomadService1 :  " + req;
		}

	}
	public static void main(String[] args) {
		System.setProperty("SERVER_PORT","8081");
		System.setProperty("server.tomcat.max-threads","1000");

		SpringApplication.run(NomadRemoteService.class, args);
	}
}
