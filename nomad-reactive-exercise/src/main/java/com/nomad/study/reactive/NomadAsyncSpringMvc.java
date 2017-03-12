package com.nomad.study.reactive;

import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

@SpringBootApplication
@EnableAsync
public class NomadAsyncSpringMvc {

	@RestController
	public static class MyController {

		@Autowired NomadService nomadService;

		RestTemplate rt = new RestTemplate();

		@GetMapping("/hello")
		public String synchello(int idx) {
			String result= rt.getForObject("http://localhost:8081/service?req={req}", String.class, "hello" + idx);
			return result;
		}

		AsyncRestTemplate art = new AsyncRestTemplate();
		@GetMapping("/asyncHello")
		public ListenableFuture<ResponseEntity<String>> asyncHello(int idx) {
			ListenableFuture<ResponseEntity<String>> res= art.getForEntity("http://localhost:8081/service?req={req}", String.class, "hello" + idx);
			return res;
		}

		AsyncRestTemplate nart = new AsyncRestTemplate(new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1)));
		@GetMapping("/nonblockAsyncHello")
		public ListenableFuture<ResponseEntity<String>> nonblockAsyncHello(int idx) {
			ListenableFuture<ResponseEntity<String>> res= nart.getForEntity("http://localhost:8081/service?req={req}", String.class, "hello" + idx);
			return res;
		}

		@GetMapping("/deferredAsyncHello")
		public DeferredResult<String> deferredAsyncHello(int idx) {
			DeferredResult<String> dr = new DeferredResult<>();
			ListenableFuture<ResponseEntity<String>> lf= nart.getForEntity("http://localhost:8081/service?req={req}", String.class, "hello" + idx);
			lf.addCallback(s -> {
				ListenableFuture<String> stringListenableFuture = nomadService.asyncService(s.getBody());
				stringListenableFuture.addCallback(s2 -> dr.setResult(s2), e -> dr.setErrorResult(e));
			},e -> {
				dr.setErrorResult(e);
			});

			return dr;
		}
	}

	@Service
	public static class NomadService {
	    @Async
		public ListenableFuture<String> asyncService(String req) {
			return new AsyncResult<>(req + "/AsyncResult");
		}
	}

	@Bean
	public ThreadPoolTaskExecutor nomadThreadPool() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(1);
		threadPoolTaskExecutor.setMaxPoolSize(10);
		threadPoolTaskExecutor.initialize();
		return threadPoolTaskExecutor;
	}

	public static void main(String[] args) {
		SpringApplication.run(NomadAsyncSpringMvc.class, args);
	}
}
