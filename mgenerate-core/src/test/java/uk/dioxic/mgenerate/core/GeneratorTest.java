package uk.dioxic.mgenerate.core;

import org.bson.Document;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.json.JsonWriterSettings;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import uk.dioxic.mgenerate.core.codec.*;
import uk.dioxic.mgenerate.core.util.BsonUtil;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.shouldHaveThrown;

public class GeneratorTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void documentTest() {
        CodecRegistry registry = CodecRegistries.fromProviders(asList(new ValueCodecProvider(),
                new BsonValueCodecProvider(),
                new DocumentCodecProvider(new OperatorTransformer()),
                new ExtendedCodecProvider(),
                new OperatorCodecProvider()));

        DocumentCodec codec = new DocumentCodec(registry, new BsonTypeClassMap(), new OperatorTransformer());

        Document template = BsonUtil.parseFile("src/test/resources/template.json", codec);
        Generator generator = new Generator(template);

        generator.generate(10)
                //.map(Document::toJson)
                //.doOnNext(System.out::println)
                .doOnNext(doc -> {
                    if (logger.isDebugEnabled()) {
//                        logger.debug(doc.toString());
                        logger.debug(doc.toJson(codec));
                    }
                })
//                .flatMap(d -> Flux.just(123))
                .doOnComplete(() -> System.out.println("complete"))
                .subscribe();


//        assertThat(cachedValue).as("is resolvable").isEqualTo(expected);
    }
}
