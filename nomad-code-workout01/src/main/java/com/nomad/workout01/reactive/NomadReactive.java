package com.nomad.workout01.reactive;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yhlee on 2016. 11. 28..
 */
public class NomadReactive {

    public static void main(String[] args) {

        MyObservable observable = new MyObservable();
        observable.addObserver(new MyObserver());

        ExecutorService ex = Executors.newSingleThreadExecutor();
        ex.execute(observable);
        System.out.println(Thread.currentThread().getName() + " - EXIT");

    }

    static class MyObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            System.out.println(Thread.currentThread().getName() + " - " + arg);
        }
    }


    static class MyObservable extends Observable implements Runnable {
        @Override
        public void run() {
           for ( int i = 0 ; i<10 ; i++) {
               setChanged();
               notifyObservers(i);
           }
        }
    }

}
