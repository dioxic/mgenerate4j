package uk.dioxic.mgenerate;

import uk.dioxic.faker.Faker;
import uk.dioxic.faker.resolvable.Resolvable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class FakerUtil {

	private static final Faker faker = Faker.instance(Locale.UK);

	public static Faker instance() {
		return faker;
	}

	public static String getValue(String fakerKey) {
	    return faker.get(fakerKey);
    }

    public static Resolvable<String> getResolvable(String fakerKey){
	 return faker.getResolvable(fakerKey);
    }

	public static Random random() {
        return ThreadLocalRandom.current();
    }

    public static int numberBetween(int min, int max) {
	    return random().ints(min, max).findFirst().getAsInt();
        //return random().nextInt(max - min +1) + min;
    }

    public static long numberBetween(long min, long max) {
	    return random().longs(min, max).findFirst().getAsLong();
        //return Double.valueOf(random().nextDouble() * (max - min)).longValue() + min;
    }

    public static double randomDouble(double min, double max) {
	    return random().doubles(min, max).findFirst().getAsDouble();
//        return random().nextDouble() * (max - min +1) + min;
    }

    public static BigDecimal randomDecimal(long min, long max, int scale) {
        BigDecimal decimal = new BigDecimal(randomDouble(min, max));
        return decimal.setScale(scale, RoundingMode.FLOOR);
    }

    public static boolean randomBoolean() {
	    return random().nextBoolean();
    }

    public static LocalDateTime randomDate(LocalDateTime min, LocalDateTime max) {
        Instant minInstant = min.toInstant(ZoneOffset.UTC);
        Instant maxInstant = max.toInstant(ZoneOffset.UTC);
	    return LocalDateTime.ofInstant(randomInstant(minInstant, maxInstant), ZoneOffset.UTC);
    }

    public static Instant randomInstant(Instant min, Instant max) {
	    return Instant.ofEpochMilli(numberBetween(min.toEpochMilli(), max.toEpochMilli()));
    }
}
