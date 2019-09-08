package uk.dioxic.mgenerate.core.operator.geo;

import org.bson.Document;
import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;
import uk.dioxic.mgenerate.core.operator.location.Coordinates;
import uk.dioxic.mgenerate.core.operator.location.CoordinatesBuilder;

import java.util.List;

import static java.util.Arrays.asList;

@Operator
public class Point implements Resolvable<Document>, Initializable {

    @OperatorProperty
    List<Number> long_lim = asList(-180d, 180d);

    @OperatorProperty
    List<Number> lat_lim = asList(-90d, 90d);

    private Coordinates coordinates;

    @Override
    public Document resolve() {
        Document doc = new Document();
        doc.put("type", "Point");
        doc.put("coordinates", coordinates.resolve());

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
