package com.dioxic.mgenerate;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FakerTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

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
			logger.debug("Producing {} zipcodes took {}ms ({}ms per record)", ITERATIONS, end-start, avg);
		}
	}

}
