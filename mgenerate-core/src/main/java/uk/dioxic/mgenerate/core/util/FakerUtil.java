package uk.dioxic.mgenerate.core.util;

import uk.dioxic.faker.Faker;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.core.resolver.FakerResolver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class FakerUtil {

	private static final Faker faker = Faker.instance(Locale.getDefault());

	public static Faker instance() {
		return faker;
	}

	public static String getValue(String fakerKey) {
	    return faker.get(fakerKey);
    }

    public static Resolvable<String> getResolvable(String fakerKey){
	 return new FakerResolver(faker, fakerKey);
    }

	public static Random random() {
        return ThreadLocalRandom.current();
    }

    /**
     * @param min inclusive
     * @param max exclusive
     * @return random number between bounds
     */
    public static int numberBetween(int min, int max) {
	    return random().ints(min, max).findFirst().orElse(0);
        //return random().nextInt(max - min +1) + min;
    }

    /**
     * @param min inclusive
     * @param max exclusive
     * @return random number between bounds
     */
    public static long numberBetween(long min, long max) {
	    return random().longs(min, max).findFirst().orElse(0L);
    }

    /**
     * @param min inclusive
     * @param max exclusive
     * @return random double between bounds
     */
    public static double randomDouble(Number min, Number max) {
        return randomDouble(min.doubleValue(), max.doubleValue());
    }

    /**
     * @param min inclusive
     * @param max exclusive
     * @return random double between bounds
     */
    public static double randomDouble(double min, double max) {
	    return random().doubles(min, max).findFirst().orElse(0d);
    }

    /**
     * @param min inclusive
     * @param max exclusive
     * @return random double between bounds
     */
    public static double randomDouble(int min, int max) {
	    return random().doubles(min, max).findFirst().orElse(0d);
    }

    /**
     * @param min inclusive
     * @param max exclusive
     * @return random number between bounds
     */
    public static BigDecimal randomDecimal(long min, long max, int scale) {
        BigDecimal decimal = new BigDecimal(randomDouble(min, max));
        return decimal.setScale(scale, RoundingMode.FLOOR);
    }

    public static boolean randomBoolean() {
	    return random().nextBoolean();
    }

    /**
     * @param min inclusive
     * @param max exclusive
     * @return random date between bounds
     */
    public static LocalDateTime randomDate(LocalDateTime min, LocalDateTime max) {
        Instant minInstant = min.toInstant(ZoneOffset.UTC);
        Instant maxInstant = max.toInstant(ZoneOffset.UTC);
	    return LocalDateTime.ofInstant(randomInstant(minInstant, maxInstant), ZoneOffset.UTC);
    }

    /**
     * @param min inclusive
     * @param max exclusive
     * @return random instant between bounds
     */
    public static Instant randomInstant(Instant min, Instant max) {
	    return Instant.ofEpochMilli(numberBetween(min.toEpochMilli(), max.toEpochMilli()));
    }
}
