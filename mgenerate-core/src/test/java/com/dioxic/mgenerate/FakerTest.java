package com.dioxic.mgenerate;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.faker.Faker;

import java.util.Locale;

public class FakerTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void testZipCode() {
		Integer ITERATIONS = 50;

		Faker f = Faker.instance(Locale.UK);

		String name = f.get("name.first_name");

//		for (int i = 0; i < 10; i++) {
//			// System.out.println(Faker.instance(Locale.UK).address().zipCode());
//			Long start = System.currentTimeMillis();
//
//			for (int j = 0; j < ITERATIONS; j++) {
//				f.address().zipCode();
//			}
//			Long end = System.currentTimeMillis();
//			Double avg = (end.doubleValue() - start.doubleValue()) / ITERATIONS.doubleValue();
//			logger.debug("Producing {} zipcodes took {}ms ({}ms per record)", ITERATIONS, end-start, avg);
//		}
	}

}
