package com.nomad.study.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

//@SpringBootApplication
@Slf4j
//@EnableAsync
public class TobystudyApplication {

	@Component
	public static class MyService {

	    @Async
//		public Future<String> hello() throws InterruptedException {
		public ListenableFuture<String> hello() throws InterruptedException {
			log.info("start Hello()");
			Thread.sleep(2000);
			return new AsyncResult<>("hello");
		}

	}

	@Bean
	/**
	 * 이 bean을 선언하지 않으면 SimpleExecutorTask 쓰레드가 생성되는데 Product 에서는 피해야 함
	 */
	ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor tp = new ThreadPoolTaskExecutor();
		tp.setCorePoolSize(10); // change this value in runtime via JMX
		tp.setMaxPoolSize(50);
		tp.setQueueCapacity(200);
		tp.setThreadNamePrefix("nomadThread-");
		return tp;
	}


	public static void main(String[] args) {
		try (ConfigurableApplicationContext c = SpringApplication.run(TobystudyApplication.class, args)) {
		}

	}

	@Autowired MyService myService;

	@Bean
	ApplicationRunner runner() {
		return args -> {
			log.info("Start Runner!!");
//			Future<String> res = myService.hello();
			ListenableFuture<String> res = myService.hello();
			res.addCallback(s -> System.out.println(s),e-> System.out.println(e.getMessage()));
			log.info("Exit : " + res.isDone());
			log.info("Result : " + res.get());
		};
	}
}
