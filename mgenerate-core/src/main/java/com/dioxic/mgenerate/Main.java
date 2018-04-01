package com.dioxic.mgenerate;

import org.bson.Document;
import org.bson.json.JsonWriterSettings;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static com.dioxic.mgenerate.JsonUtil.parse;
import static com.dioxic.mgenerate.JsonUtil.toJson;

public class Main {

    public static void main(String[] args) {
        Integer ITERATIONS = args.length > 1 ? Integer.valueOf(args[1]) : 100;

        Document doc = parse(args[0]);

        Long start = System.currentTimeMillis();

        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get("output.json")))) {
            Stream.generate(() -> toJson(doc))
                    .limit(ITERATIONS)
                    .parallel()
                    .forEach(pw::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Long end = System.currentTimeMillis();
        Double avg = (end.doubleValue() - start.doubleValue()) / ITERATIONS.doubleValue();
        System.out.printf("Producting %s documents took %sms (%sms per record)%n", ITERATIONS, end - start, avg);

    }

}
