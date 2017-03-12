package com.nomad.study.reactive;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * Created by yhlee on 2017. 1. 8..
 */
@Slf4j
public class FutureTaskEx {

    interface SuccessCallback {
        void onSuccess(String result);
    }

    interface ErrorCallback {
        void onError(Throwable t);
    }


    static class CallbackFutureTask extends FutureTask<String> {
        SuccessCallback sc;
        ErrorCallback ec;

        public CallbackFutureTask(Callable callable, SuccessCallback sc, ErrorCallback ec) {
            super(callable);
            this.sc = Objects.requireNonNull(sc);
            this.ec = Objects.requireNonNull(ec);
        }

        @Override
        protected void done() {
            try {
                this.sc.onSuccess(get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
//                e.printStackTrace();
            } catch (ExecutionException e) {
                ec.onError(e.getCause());
            }
        }
    }


    public static void main(String[] args) {

        ExecutorService es = Executors.newCachedThreadPool();
        CallbackFutureTask f = new CallbackFutureTask(() -> {
            Thread.sleep(2000);
//            if (1 == 1) throw new RuntimeException("Error!!!");
            log.info("Async Hello");
            return "Hello";
        }, res -> log.info("onSuccessCallback : " + res),
                e -> log.info("onError" + e.getMessage()));

        es.execute(f);
        es.shutdown();

        log.info("Program End!!");


    }

}
