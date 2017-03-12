package com.nomad.workout01.reactor;

import reactor.core.publisher.Flux;

/**
 * Created by yhlee on 2016. 12. 12..
 */
public class ReactorEx {
    public static void main(String[] args) {
        Flux.create(e -> {
            e.next(1);
            e.next(2);
            e.next(3);
        })
                .subscribe(System.out::println);
    }
}
