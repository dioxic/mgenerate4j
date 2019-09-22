package uk.dioxic.mgenerate.core.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.mgenerate.core.operator.geo.Coordinates;
import uk.dioxic.mgenerate.core.operator.geo.CoordinatesBuilder;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class FastLocalSearchTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    void randomPoints() {
        Coordinates coord = new CoordinatesBuilder(ReflectiveTransformerRegistry.getInstance()).build();
        uk.dioxic.mgenerate.core.operator.type.Coordinates[] points = Stream.generate(coord::resolveInternal)
                .limit(5)
                .toArray(uk.dioxic.mgenerate.core.operator.type.Coordinates[]::new);

        logger.debug("before: {}", Arrays.stream(points).map(uk.dioxic.mgenerate.core.operator.type.Coordinates::toString).collect(Collectors.joining(" ")));

        GeoUtil.optimise(points);

        logger.debug("after: {}", Arrays.stream(points).map(uk.dioxic.mgenerate.core.operator.type.Coordinates::toString).collect(Collectors.joining(" ")));
    }

}
