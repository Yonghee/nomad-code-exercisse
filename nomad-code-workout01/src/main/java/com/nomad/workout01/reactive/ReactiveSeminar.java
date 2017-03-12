package com.nomad.workout01.reactive;


import java.util.Arrays;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by yhlee on 2016. 12. 3..
 */
@SuppressWarnings("deprecation")
public class ReactiveSeminar {

    public static void main(String[] args) throws InterruptedException {

        Iterable<Integer> iterable = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Iterator<Integer> it = iterable.iterator();
        ExecutorService es = Executors.newSingleThreadExecutor();

        class SimpleObservable extends Observable implements Runnable {
            @Override
            public synchronized void addObserver(Observer o) {
                super.addObserver(o);
                System.out.println(Thread.currentThread().getName() + ": addObserver");
            }

            @Override
            public void run() {
               iterable.forEach(i -> {
                   setChanged();
                   notifyObservers(i);
               });
            }
        }

        Observer observer = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(Thread.currentThread().getName() + " : " + arg);
            }
        };

        SimpleObservable simpleObservable = new SimpleObservable();
        simpleObservable.addObserver(observer);

        es.execute(simpleObservable);
        System.out.println(Thread.currentThread().getName() + ": Exit");
        es.shutdown();


        /*

        ExecutorService es = Executors.newSingleThreadExecutor();

        Publisher<Integer> pub = new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> subscriber) {
                System.out.println(Thread.currentThread().getName() + ": subscribe");
               subscriber.onSubscribe(new Subscription() {
                   @Override
                   public void request(long n) {
                       es.execute( () -> {
                           try {
                               Long l = n;
                               while (--l >= 0) {
                                   if (it.hasNext()) {
                                       subscriber.onNext(it.next());
                                   }else {
                                       subscriber.onComplete();
                                       break;
                                   }
                               }
                           } catch (RuntimeException e) {
                               subscriber.onError(e);
                           }
                       });

                   }

                   @Override
                   public void cancel() {
                       System.out.println("Cancel Request!!");
                       //exit;;
                   }
               });
            }
        };

        Subscriber<Integer> sub = new Subscriber<Integer>() {
            Subscription subscription;
            boolean isCompleted = false;
            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                System.out.println(Thread.currentThread().getName() + ": onSubscribe");
                this.subscription.request(1);
            }

            @Override
            public void onNext(Integer item) {
                System.out.println(Thread.currentThread().getName() + " : " + item);
                if ( !isCompleted) subscription.request(1);

            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError : " + throwable.getClass().getName());
                this.subscription.cancel();
            }

            @Override
            public void onComplete() {
                System.out.println("onCompleted");
                this.isCompleted = true;
            }
        };

        pub.subscribe(sub);
        System.out.println(Thread.currentThread().getName() + ": Exit");
        es.awaitTermination(5, TimeUnit.SECONDS);
        es.shutdown();
        */


    }


    /**
     *
     ExecutorService es = Executors.newSingleThreadExecutor();

     class MyObservable extends Observable implements Runnable {
    @Override
    public synchronized void addObserver(Observer o) {
    System.out.println(Thread.currentThread().getName() + ": addObserver");
    super.addObserver(o);
    }

    public void run() {
    iterable.forEach(i -> {
    setChanged();
    notifyObservers(i);
    });
    }
    }

     MyObservable observable = new MyObservable();

     Observer sub = new Observer() {
    @Override
    public void update(Observable o, Object arg) {
    System.out.println(Thread.currentThread().getName() + ": " + arg);
    }
    };

     observable.addObserver(sub);
     es.execute(observable);
     System.out.println(Thread.currentThread().getName() + ": Exit");
     es.shutdown();
     */
}
