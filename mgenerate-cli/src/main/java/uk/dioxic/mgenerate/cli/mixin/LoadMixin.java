package uk.dioxic.mgenerate.cli.mixin;

import com.mongodb.reactivestreams.client.MongoCollection;
import lombok.Getter;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;
import reactor.core.publisher.Mono;
import uk.dioxic.mgenerate.cli.Generator;
import uk.dioxic.mgenerate.cli.LoadRunner;

@Getter
public class LoadMixin {

    @Mixin
    MongoMixin mongoMixin;

    @Option(names = {"--drop"},
            description = "drop collection before loading data (default: ${DEFAULT-VALUE})",
            defaultValue = "false")
    private boolean drop;

    @Option(names = {"-b", "--batchSize"},
            description = "operation batch size (default: ${DEFAULT-VALUE})",
            defaultValue = "1000",
            paramLabel = "arg")
    private int batchSize;

    @Option(names = {"--concurrency"},
            description = "reactive flapmap concurrency (default: ${DEFAULT-VALUE})",
            defaultValue = "4",
            paramLabel = "arg")
    private int concurrency;

//    public <T> void load(Generator<T> generator) {
//        long start = System.currentTimeMillis();
//
////        mongoMixin.addCodecRegistry(generator.codecRegistry());
//        MongoCollection<T> collection = mongoMixin.getCollection(generator.getModelClass());
//
//        LoadRunner<T> loadRunner = LoadRunner.<T>builder()
//                .generator(generator)
//                .batchSize(batchSize)
//                .collection(collection)
//                .recordsPerOperation(schema.recordsPerOperation(generator))
//                .schema(schema)
//                .concurrency(concurrency)
//                .build();
//
//        if (drop) {
//            Mono.from(collection.drop()).block();
//        }
//
//        schema.indexModel(collection).block();
//
//        loadRunner.load().block();
//
//        long time = (System.currentTimeMillis() - start) / 1000;
//        System.out.println("Loaded " + generator.recordCount() + " records in " + time + "s");
//    }
}
