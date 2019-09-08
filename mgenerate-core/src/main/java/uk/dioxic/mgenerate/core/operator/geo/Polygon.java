package uk.dioxic.mgenerate.core.operator.geo;

import org.bson.Document;
import uk.dioxic.mgenerate.common.Cache;
import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.ReflectiveTransformerRegistry;
import uk.dioxic.mgenerate.core.operator.location.Coordinates;
import uk.dioxic.mgenerate.core.operator.location.CoordinatesBuilder;
import uk.dioxic.mgenerate.core.util.FlsUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

@Operator
public class Polygon implements Resolvable<Document>, Initializable {

    @OperatorProperty
    List<Number> long_lim = asList(-180d, 180d);

    @OperatorProperty
    List<Number> lat_lim = asList(-90d, 90d);

    @OperatorProperty
    Resolvable<Integer> corners = Wrapper.wrap(3);

    @OperatorProperty
    Boolean valid = true;

    private Coordinates coordinates;

    @Override
    public Document resolve() {
        return resolve(null);
    }

    @Override
    public Document resolve(Cache cache) {
        FlsUtil.Point[] polygon = Stream.generate(() -> coordinates.resolve(cache))
                .limit(corners.resolve(cache))
                .toArray(FlsUtil.Point[]::new);

        if (valid) {
            FlsUtil.optimise(polygon);
        }

        List<FlsUtil.Point> polygonList = new ArrayList<>(polygon.length+1);
        polygonList.addAll(asList(polygon));
        polygonList.add(polygonList.get(0));

        Document doc = new Document();
        doc.put("type", "Polygon");
        doc.put("coordinates", Collections.singletonList(polygonList));

        return doc;
    }

    @Override
    public void initialize() {
        coordinates = new CoordinatesBuilder(ReflectiveTransformerRegistry.getInstance())
                .long_lim(long_lim)
                .lat_lim(lat_lim)
                .build();
    }
}
