package uk.dioxic.mgenerate.core;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.mgenerate.core.operator.location.Coordinates;
import uk.dioxic.mgenerate.core.operator.location.CoordinatesBuilder;
import uk.dioxic.mgenerate.core.util.FlsUtil;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FastLocalSearchTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void randomPoints() {
        Coordinates coord = new CoordinatesBuilder().build();
        FlsUtil.Point[] points = Stream.generate(coord::resolve)
                .limit(5)
                .toArray(FlsUtil.Point[]::new);

        logger.debug("before: {}", Arrays.stream(points).map(FlsUtil.Point::toString).collect(Collectors.joining(" ")));

        FlsUtil.optimise(points);

        logger.debug("after: {}", Arrays.stream(points).map(FlsUtil.Point::toString).collect(Collectors.joining(" ")));
    }

}
