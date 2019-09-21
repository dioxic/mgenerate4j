package uk.dioxic.mgenerate.core.operator.geo;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class PointTest {

    @Test
    void resolve() {
        List<Number> longBounds = asList(0d, 10d);
        List<Number> latBounds = asList(-20, 0);

        Point point = new PointBuilder(ReflectiveTransformerRegistry.getInstance()).long_lim(longBounds).lat_lim(latBounds).build();

        assertThat(point.resolve()).isNotNull();
        assertThat(point.resolve().get("type")).as("geo type").isEqualTo("Point");
        assertThat(point.resolve().get("coordinates")).as("coordinates class type").isInstanceOf(uk.dioxic.mgenerate.core.operator.type.Coordinates.class);

        uk.dioxic.mgenerate.core.operator.type.Coordinates p = (uk.dioxic.mgenerate.core.operator.type.Coordinates) point.resolve().get("coordinates");
        assertThat(p.getX()).as("longitude").isBetween(longBounds.get(0).doubleValue(), longBounds.get(1).doubleValue());
        assertThat(p.getY()).as("latitude").isBetween(latBounds.get(0).doubleValue(), latBounds.get(1).doubleValue());
    }
}
