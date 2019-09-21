package uk.dioxic.mgenerate.core.operator.geo;

import org.bson.Document;
import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;
import uk.dioxic.mgenerate.core.util.GeoUtil;

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
        uk.dioxic.mgenerate.core.operator.type.Coordinates[] polygon = Stream.generate(() -> coordinates.resolve())
                .limit(corners.resolve())
                .toArray(uk.dioxic.mgenerate.core.operator.type.Coordinates[]::new);

        if (valid) {
            GeoUtil.optimise(polygon);
        }

        List<uk.dioxic.mgenerate.core.operator.type.Coordinates> polygonList = new ArrayList<>(polygon.length+1);
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
