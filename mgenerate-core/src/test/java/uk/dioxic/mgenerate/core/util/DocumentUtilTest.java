package uk.dioxic.mgenerate.core.util;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentUtilTest {

    @Test
    void flatMap_NoEmbedding() {
        Document document = new Document();
        document.put("name", "bob");
        document.put("age", 20);

        Map<String, Object> flatMap = DocumentUtil.flatMap(document);

        assertThat(flatMap).containsEntry("name", "bob");
        assertThat(flatMap).containsEntry("age", 20);
    }

    @Test
    void flatMap_SimpleArray() {
        List<Object> array1 = Arrays.asList("kevin", "perry");
        Document document = new Document("array", array1);

        Map<String, Object> actual = DocumentUtil.flatMap(document);

        assertThat(actual).containsEntry("array", array1);
        assertThat(actual).containsEntry("array.0", "kevin");
        assertThat(actual).containsEntry("array.1", "perry");
    }

    @Test
    void flatMap_NestedArray() {
        List<Object> array2 = Arrays.asList("kevin", "perry");
        Document embedded = new Document("embeddedArray", array2);
        List<Object> array1 = Arrays.asList("bob", embedded);

        Document document = new Document("array", array1);

        Map<String, Object> actual = DocumentUtil.flatMap(document);

        assertThat(actual).containsEntry("array", array1);
        assertThat(actual).containsEntry("array.0", "bob");
        assertThat(actual).containsEntry("array.1", embedded);
        assertThat(actual).containsEntry("array.1.embeddedArray", array2);
        assertThat(actual).containsEntry("array.1.embeddedArray.0", "kevin");
        assertThat(actual).containsEntry("array.1.embeddedArray.1", "perry");
    }

    @Test
    void flatMap_SubDocument() {
        Document document = new Document();
        Document d2 = new Document("name", "mary");
        Document d1 = new Document("embedded", d2);
        document.put("subdoc", d1);
        document.put("name", "gary");

        Map<String, Object> actual = DocumentUtil.flatMap(document);

        assertThat(actual).containsEntry("name", "gary");
        assertThat(actual).containsEntry("subdoc", d1);
        assertThat(actual).containsEntry("subdoc.embedded", d2);
        assertThat(actual).containsEntry("subdoc.embedded.name", "mary");
    }

    @Test
    void coordinateLookup_SingleField() {
        Document document = new Document();
        document.put("name", "bob");
        document.put("age", 20);

        assertThat(DocumentUtil.coordinateLookup("name", document)).isEqualTo("bob");
        assertThat(DocumentUtil.coordinateLookup("age", document)).isEqualTo(20);
    }

    @Test
    void coordinateLookup_EmbeddedSingleField() {
        Document document = new Document("subdoc", new Document("embedded", "bob"));

        assertThat(DocumentUtil.coordinateLookup("subdoc.embedded", document)).isEqualTo("bob");
    }

    @Test
    void coordinateLookup_ArrayField() {
        List<Object> array = Arrays.asList("kevin", "perry");
        Document document = new Document("array", array);

        assertThat(DocumentUtil.coordinateLookup("array", document)).isEqualTo(array);
    }

    @Test
    @SuppressWarnings("unchecked")
    void coordinateLookup_EmeddedArrayField() {
        List<Object> array = Arrays.asList(
                new Document("name", "kevin"),
                new Document("name", "bob"),
                new Document("name", "perry")
        );
        Document document = new Document("array", array);

        Object actual = DocumentUtil.coordinateLookup("array.name", document);

        assertThat(actual).isInstanceOf(List.class);
        assertThat((List) actual).containsExactlyInAnyOrder("kevin", "bob", "perry");
    }

    @Test
    @SuppressWarnings("unchecked")
    void coordinateLookup_DeepEmeddedArrayField() {
        List<Object> array = Arrays.asList(
                new Document("people", Arrays.asList(
                        new Document("name", "kevin"),
                        new Document("name", "perry")
                )),
                new Document("people", new Document("name", "bob"))
        );
        Document document = new Document("array", array);

        Object actual = DocumentUtil.coordinateLookup("array.people.name", document);

        assertThat(actual).isInstanceOf(List.class);
        assertThat((List) actual).containsExactlyInAnyOrder("kevin", "bob", "perry");
    }
}
