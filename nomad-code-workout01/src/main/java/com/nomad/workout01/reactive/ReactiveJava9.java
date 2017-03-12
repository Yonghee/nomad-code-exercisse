package com.nomad.workout01.reactive;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.*;

/**
 * Created by yhlee on 2016. 12. 2..
 */
@SuppressWarnings("deprecation")
public class ReactiveJava9 {

    public static void main(String[] args) throws InterruptedException {

        Iterable<Integer> iterable = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

//        imperative(iterable);
//        ObserverblPrint(iterable);
        reactivRunner(iterable);

    }

    private static <T> void reactivRunner(Iterable<T> iterable) throws InterruptedException {
        ExecutorService es = Executors.newCachedThreadPool();

        Publisher<T> pub =
                (Subscriber<? super T> subscriber) -> {
                    Iterator<T> it = iterable.iterator();
                    subscriber.onSubscribe(new Subscription() {
                        @Override
                        public void request(long number) {
                            es.execute(() -> {
                                Long n = number;
                                try {
                                    while (--n >= 0) {
                                        if (it.hasNext()) {
                                            subscriber.onNext(it.next());
                                        } else {
                                            subscriber.onComplete();
                                            break;
                                        }
                                    }
                                } catch (Exception e) {
                                    subscriber.onError(e);
                                }
                            });
                        }

                        @Override
                        public void cancel() {

                        }
                    });

                };


        Subscriber<T> sub = new Subscriber<T>() {
            Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                System.out.println(Thread.currentThread().getName() + ": onSubscribe");
                subscription.request(1);

            }

            @Override
            public void onNext(T item) {
                System.out.println(Thread.currentThread().getName() + ": " + item);
                subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(Thread.currentThread().getName() + ": " + throwable.getClass());

            }

            @Override
            public void onComplete() {

                System.out.println(Thread.currentThread().getName() + ": Completed!!!");

            }
        };

        pub.subscribe(sub);
        System.out.println(Thread.currentThread().getName() + ": Exit");

        es.awaitTermination(5, TimeUnit.SECONDS);
        es.shutdown();


    }

    static void ObserverblPrint(Iterable<Integer> iterable) {

        class NomadObservable extends Observable implements Runnable {
            public void run() {
                iterable.forEach(i -> {
                    setChanged();
                    notifyObservers(i);
                });
            }
        }

        ExecutorService es = Executors.newSingleThreadExecutor();

        System.out.println(Thread.currentThread().getName() + ": Start");
        NomadObservable nomadObservable = new NomadObservable();
        Observer observer = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(Thread.currentThread().getName() + ": " + arg);
            }
        };

        nomadObservable.addObserver(observer);
        es.execute(nomadObservable);

        System.out.println(Thread.currentThread().getName() + ": EXIT");
        es.shutdown();
    }


    static void imperative(Iterable<Integer> iterable) {

        iterable.forEach(i -> System.out.println(i));
    }
}
