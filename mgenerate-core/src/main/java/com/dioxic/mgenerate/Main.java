package com.dioxic.mgenerate;

import com.dioxic.mgenerate.transformer.OperatorTransformer;
import com.github.javafaker.Faker;
import org.bson.Document;
import org.bson.codec.OperatorCodecProvider;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.json.JsonWriterSettings;
import org.bson.json.StrictJsonReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class Main {

    private static CodecRegistry registry = CodecRegistries.fromProviders(asList(new ValueCodecProvider(),
            new DocumentCodecProvider(new OperatorTransformer()),
            new OperatorCodecProvider()));

    private static DocumentCodec codec = new DocumentCodec(registry, new BsonTypeClassMap(), new OperatorTransformer());

    private static JsonWriterSettings jws = JsonWriterSettings.builder()
            .indent(true)
            .build();

    private static Document parse(String file) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
            StrictJsonReader bsonReader = new StrictJsonReader(json);
            return codec.decode(bsonReader, DecoderContext.builder().build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Integer ITERATIONS = args.length > 1 ? Integer.valueOf(args[1]) : 100;

        Document doc = parse(args[0]);

        // System.out.println(Faker.instance(Locale.UK).address().zipCode());
        Long start = System.currentTimeMillis();

        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get("output.json")))) {
            Stream.generate(() -> doc.toJson(jws, codec))
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
