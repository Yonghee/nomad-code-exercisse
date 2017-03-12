package com.nomad.study.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yhlee on 2017. 1. 8..
 */
@Slf4j
public class MyLoadRunner {

	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		ExecutorService es = Executors.newFixedThreadPool(101);
		AtomicInteger counter = new AtomicInteger();

		RestTemplate rt = new RestTemplate();
//		String url = "http://localhost:8080/nonblockAsyncHello?idx={idx}";
		String url = "http://localhost:8080/deferredAsyncHello?idx={idx}";
		//        String url = "http://localhost:8080/callable";

		CyclicBarrier barrier = new CyclicBarrier(101);

		for (int i = 0; i < 100; i++) {
			es.submit(() -> {

				StopWatch thSw = new StopWatch();
				thSw.start();
				int threadIdx = counter.addAndGet(1);

				barrier.await();

				log.info("Thread : [{}] started",threadIdx);
				String res = rt.getForObject(url, String.class,threadIdx);
				thSw.stop();
				double elapsedTime = thSw.getTotalTimeSeconds();
				log.info("Thread End : ThreadId->{}, ElapsedTime ->{} , RES -> {}", threadIdx, elapsedTime, res);

				return null;

			});
		}

		barrier.await();

		StopWatch main = new StopWatch();
		main.start();

		es.shutdown();
		es.awaitTermination(100, TimeUnit.SECONDS);

		main.stop();
		log.info("Total Elapsed Time : {}", main.getTotalTimeSeconds());

	}
}
