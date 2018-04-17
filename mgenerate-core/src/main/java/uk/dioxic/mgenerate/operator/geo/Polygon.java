package uk.dioxic.mgenerate.operator.geo;

import org.bson.Document;
import uk.dioxic.faker.resolvable.Resolvable;
import uk.dioxic.mgenerate.Initializable;
import uk.dioxic.mgenerate.OperatorFactory;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.mgenerate.operator.location.Coordinates;
import uk.dioxic.mgenerate.operator.location.CoordinatesBuilder;
import uk.dioxic.mgenerate.util.FlsUtil;

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
    Resolvable<Integer> corners = OperatorFactory.wrap(3);

    @OperatorProperty
    Boolean valid = true;

    private Coordinates coordinates;

    @Override
    public Document resolve() {
        FlsUtil.Point[] polygon = Stream.generate(coordinates::resolve)
                .limit(corners.resolve())
                .toArray(FlsUtil.Point[]::new);

        if (valid) {
            FlsUtil.optimise(polygon);
        }

        Document doc = new Document();
        doc.put("type", "Polygon");
        doc.put("coordinates", asList(asList(polygon)));

        return doc;
    }

    @Override
    public void initialize() {
        coordinates = new CoordinatesBuilder()
                .long_lim(long_lim)
                .lat_lim(lat_lim)
                .build();
    }
}
