package uk.dioxic.mgenerate.core.operator.geo;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class LineStringTest {

    @Test
    void resolve() {
        List<Number> longBounds = asList(0d, 10d);
        List<Number> latBounds = asList(-20, 0);

        LineString lineString = new LineStringBuilder(ReflectiveTransformerRegistry.getInstance()).longBounds(longBounds).latBounds(latBounds).build();

        assertThat(lineString.resolve()).isNotNull();
        assertThat(lineString.resolve().get("type")).as("geo type").isEqualTo("LineString");
        assertThat(lineString.resolve().get("coordinates")).as("coordinates class type").isInstanceOf(List.class);

        List<?> coordinates = (List<?>)lineString.resolve().get("coordinates");

        coordinates.forEach(c -> {
            assertThat(c).isInstanceOf(uk.dioxic.mgenerate.core.operator.type.Coordinates.class);
            uk.dioxic.mgenerate.core.operator.type.Coordinates p = (uk.dioxic.mgenerate.core.operator.type.Coordinates) c;
            assertThat(p.getX()).as("longitude").isBetween(longBounds.get(0).doubleValue(), longBounds.get(1).doubleValue());
            assertThat(p.getY()).as("latitude").isBetween(latBounds.get(0).doubleValue(), latBounds.get(1).doubleValue());
        });
    }
}
