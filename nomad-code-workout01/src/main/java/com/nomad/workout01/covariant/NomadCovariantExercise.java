package com.nomad.workout01.covariant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nomad on 2016. 11. 24..
 */
public class NomadCovariantExercise {

	public static void main(String[] args) {

		Number[] numbers = new Number[10];
		numbers[0] = new Integer(1);
		numbers[2] = new Long(2L);

		A[] as = new A[3];
		as[0] = new B();
		as[1]  = new C();

		List<A> as1 = new ArrayList<>();
		as1.add(new B());

		List<B> bs = new ArrayList<>();
		// as1 = bs; // 컴파일 오류 발생함
		// bs = as1; // 컴파일 오류

		Number[] numbers1 = { 1, 2, 3 };

		numbers = new Integer[3];
//		numbers[0] = 3.14; // 컴파일은 되지만 런타임에서 예외 발생

		runArrayNumberSum();
		runListSum();
	}

	private static void runListSum() {

		List<Number> numbers = Arrays.asList(1, 2, 3);
		List<Integer> ints = Arrays.asList(1, 2, 3);
		List<Double> doubles = Arrays.asList(1.0, 2.0, 3.0);
		List<Long> longs = Arrays.asList(1L, 2L, 3L);

		System.out.println(listSum(numbers));
//		System.out.println(listSum(ints)); //컴파일 오류
		System.out.println(genericSum(doubles));
		System.out.println(genericSum(longs));
	}

	private static void runArrayNumberSum() {

		Number[] numbers1 = { 1, 2, 3 };
		Integer[] ints = { 1, 2, 3 };
		Double[] doubles = { 1.0, 2.0, 3.0 };
		Long[] longs = { 1L, 2L, 3L };

		System.out.println(arraySum(numbers1));
		System.out.println(arraySum(ints));
		System.out.println(arraySum(doubles));
		System.out.println(arraySum(longs));

	}

	static <T extends Number> long genericSum(List<T> numbers) {
		long sum = 0;
		for (T n : numbers) {
			sum += n.longValue();
		}
		return sum;
	}

	static long arraySum(Number[] numbers) {
		long sum = 0;
		for (Number n : numbers) {
			sum += n.longValue();
		}
		return sum;
	}

	static long listSum(List<Number> numberList) {
		long sum = 0;
		for (Number n : numberList) {
			sum += n.longValue();
		}
		return sum;
	}

	static class A {}
	static class B extends A{}
	static class C extends B{}




}
