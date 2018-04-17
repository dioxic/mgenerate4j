package uk.dioxic.mgenerate;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.operator.location.Coordinates;
import uk.dioxic.mgenerate.operator.location.CoordinatesBuilder;
import uk.dioxic.mgenerate.util.FlsUtil;

import java.util.Arrays;
import java.util.stream.Stream;

public class FastLocalSearchTest {

    @Test
    public void randomPoints() {
        Coordinates coord = new CoordinatesBuilder().build();
        FlsUtil.Point[] points = Stream.generate(coord::resolve)
                .limit(5)
                .toArray(FlsUtil.Point[]::new);

        Arrays.stream(points).forEach(System.out::println);

        FlsUtil.optimise(points);

        Arrays.stream(points).forEach(System.out::println);
    }

}
