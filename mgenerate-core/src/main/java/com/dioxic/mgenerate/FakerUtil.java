package com.dioxic.mgenerate;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import com.github.javafaker.Faker;

public class FakerUtil {

	private static final Faker faker = Faker.instance(Locale.UK, ThreadLocalRandom.current());

	public static Faker instance() {
		return faker;
	}
}
