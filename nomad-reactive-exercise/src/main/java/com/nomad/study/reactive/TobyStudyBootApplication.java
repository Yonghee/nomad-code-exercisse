package com.nomad.study.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

//@SpringBootApplication
@Slf4j
//@EnableAsync
public class TobyStudyBootApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext c = SpringApplication.run(TobyStudyBootApplication.class, args);
    }

    @RestController
    public static class MyController {
        @GetMapping("/async")
        public String asyncHello() throws InterruptedException {
            Thread.sleep(2000);
            return "Hello";
        }


        @GetMapping("/callable")
        public Callable<String> callableHello() throws InterruptedException {
            log.info("callable Hello method");
            return () -> {
                log.info("Callable Async method Start");
                Thread.sleep(2000);
                return "Hello";
            };
        }

        Queue<DeferredResult<String>> results = new ConcurrentLinkedQueue<>();

        @GetMapping("/deferred")
        public DeferredResult<String> deferredResult() throws InterruptedException {
            log.info("callable Hello method");
            DeferredResult<String> dr = new DeferredResult<>(60000L);
            results.add(dr);
            return dr;
        }

        @GetMapping("/deferred/count")
        public String deferredCount() {
            return String.valueOf(this.results.size());
        }

        @GetMapping("/deferred/event")
        public String eventFire(String msg) {
            for (DeferredResult<String> dr : results) {
                dr.setResult("Hello + " + msg);
            }
            return "OK";
        }
    }


}
