package com.nomad.study.reactive;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * Created by yhlee on 2017. 1. 8..
 */
@Slf4j
public class FutrueEx {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService es = Executors.newCachedThreadPool();
/*

        es.execute(() -> {
            try {
                Thread.sleep(2000);
                log.info("Hello Async");
            } catch (InterruptedException e) {}
        });
*/

        log.info("Start");
        FutureTask<String> f = new FutureTask<String>(() -> {
            Thread.sleep(2000);
            log.info("Async Hello");
            return "Hello";
        }){
            @Override
            protected void done() {
                try {
                    log.info(get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        es.execute(f);
//        Future<String> result= es.submit(() -> {
//            Thread.sleep(2000);
//            log.info("Async Hello");
//            return "Hello";
//        });

//        log.info("Processing another job....");

        log.info(f.get());
        log.info("End");


    }
}
