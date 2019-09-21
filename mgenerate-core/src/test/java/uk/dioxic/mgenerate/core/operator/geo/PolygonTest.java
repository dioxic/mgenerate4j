package uk.dioxic.mgenerate.core.operator.geo;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class PolygonTest {

    @Test
    @SuppressWarnings("unchecked")
    void polygon() {
        int corners = 5;
        List<Number> longBounds = asList(0d, 100d);
        List<Number> latBounds = asList(-200, 0);
        Polygon polygon = new PolygonBuilder(ReflectiveTransformerRegistry.getInstance())
                .corners(corners)
                .long_lim(longBounds)
                .lat_lim(latBounds)
                .build();

        assertThat(polygon.resolve().get("type")).as("geo type").isEqualTo("Polygon");
        assertThat(polygon.resolve().get("coordinates")).as("coordinates class").isInstanceOf(List.class);
        assertThat(polygon.resolve().get("coordinates")).asList().hasSize(1);

        List<uk.dioxic.mgenerate.core.operator.type.Coordinates> polygonPoints = (List<uk.dioxic.mgenerate.core.operator.type.Coordinates>) ((List) polygon.resolve().get("coordinates")).get(0);

        assertThat(polygonPoints).hasSize(corners+1);
        assertThat(polygonPoints.get(0)).as("first and last points equivalent").isEqualTo(polygonPoints.get(polygonPoints.size()-1));
        polygonPoints.forEach(p -> {
            assertThat(p.getX()).as("longitude").isBetween(longBounds.get(0).doubleValue(), longBounds.get(1).doubleValue());
            assertThat(p.getY()).as("latitude").isBetween(latBounds.get(0).doubleValue(), latBounds.get(1).doubleValue());
        });
    }
}
