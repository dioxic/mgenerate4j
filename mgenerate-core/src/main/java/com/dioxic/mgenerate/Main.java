package com.dioxic.mgenerate;

import com.github.javafaker.Faker;

import java.util.Locale;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Integer ITERATIONS = 10;

        //Thread.sleep(10000L);

        Faker f = Faker.instance(Locale.UK);

        for (int i = 0; i < 10; i++) {
            // System.out.println(Faker.instance(Locale.UK).address().zipCode());
            Long start = System.currentTimeMillis();

            for (int j = 0; j < ITERATIONS; j++) {
                f.address().zipCode();
            }
            Long end = System.currentTimeMillis();
            Double avg = (end.doubleValue() - start.doubleValue()) / ITERATIONS.doubleValue();
            System.out.printf("Producting %s zipcodes took %sms (%sms per record)%n", ITERATIONS, end - start, avg);
        }

    }

}
