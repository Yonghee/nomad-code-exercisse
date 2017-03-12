package com.nomad.workout01.java8;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by nomad on 2016. 11. 23..
 */
public class LamdaExec {

	public static void main(String[] args) {

		List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

//		List<Integer> gt4 = filter(list1, i ->   (i > 4)? true:false );
//		System.out.println(gt4);
//
//		List<Integer> evenNumbers = filter(list1,i -> ( (i % 2) ==0)? true:false);
//		System.out.println(evenNumbers);

		List<Integer> result1= list1.stream().filter(i -> (i % 3) == 0 ? true : false).collect(toList());
		System.out.println(result1);

		long count = list1.stream().map(i -> i * 10).filter(i -> (i > 4) ? true : false).count();
		System.out.println(count);

	}
/*
	static <T> List<T> filter(List<T> input, Predicate<? super T> p) {
		List<T> result = new ArrayList<T>();
		p.hello();
		for (T t : input) {
			if (p.test(t)) {
				result.add(t);
			}
		}
		return result;
	}

	@FunctionalInterface
	interface Predicate<T> {
		boolean test(T t);

		default void hello() {
			System.out.println("Hello");
		}
	}*/
}
