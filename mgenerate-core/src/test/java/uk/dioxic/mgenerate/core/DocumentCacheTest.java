package uk.dioxic.mgenerate.core;

import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.mgenerate.core.util.BsonUtil;

import static org.assertj.core.api.Assertions.*;

public class DocumentCacheTest {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static JsonWriterSettings jws = JsonWriterSettings.builder()
            .indent(true)
            .build();

    @Test
    public void documentTest() {
        Document doc = BsonUtil.parseFile("src/test/resources/lookup-test.json");
        DocumentValueCache dvc = DocumentValueCache.getInstance();
        dvc.mapTemplate(doc);
        dvc.getKeys(doc).forEach(logger::debug);

        String outJson = BsonUtil.toJson(doc, jws);
        logger.debug(outJson);

        dvc.setEncodingContext(doc);
        Object cachedValue = dvc.get(doc, "c3");
        Object expected  = String.format("%s <%s>", dvc.get(doc, "b"), dvc.get(doc, "c.cc.ccc"));
        assertThat(cachedValue).as("is resolvable").isEqualTo(expected);
    }
}
