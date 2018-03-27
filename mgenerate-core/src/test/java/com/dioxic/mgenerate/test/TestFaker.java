package com.dioxic.mgenerate.test;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.github.javafaker.Faker;

public class TestFaker {

	@Test
	public void testZipCode() {
		Integer ITERATIONS = 50;

		Faker f = Faker.instance(Locale.UK);

		for (int i = 0; i < 10; i++) {
			// System.out.println(Faker.instance(Locale.UK).address().zipCode());
			Long start = System.currentTimeMillis();

			for (int j = 0; j < ITERATIONS; j++) {
				f.address().zipCode();
			}
			Long end = System.currentTimeMillis();
			Double avg = (end.doubleValue() - start.doubleValue()) / ITERATIONS.doubleValue();
			System.out.printf("Producting %s zipcodes took %sms (%sms per record)%n", ITERATIONS, end-start, avg);
		}
	}

}
