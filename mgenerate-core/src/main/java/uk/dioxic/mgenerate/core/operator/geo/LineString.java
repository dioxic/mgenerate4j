package uk.dioxic.mgenerate.core.operator.geo;

import org.bson.Document;
import uk.dioxic.faker.resolvable.Resolvable;
import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.OperatorFactory;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.location.Coordinates;
import uk.dioxic.mgenerate.core.operator.location.CoordinatesBuilder;
import uk.dioxic.mgenerate.core.util.FlsUtil;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

@Operator
public class LineString implements Resolvable<Document>, Initializable {

    @OperatorProperty
    List<Number> long_lim = asList(-180d, 180d);

    @OperatorProperty
    List<Number> lat_lim = asList(-90d, 90d);

    @OperatorProperty
    Resolvable<Integer> locs = OperatorFactory.wrap(3);

    @OperatorProperty
    Boolean valid = true;

    private Coordinates coordinates;

    @Override
    public Document resolve() {
        FlsUtil.Point[] polygon = Stream.generate(coordinates::resolve)
                .limit(locs.resolve())
                .toArray(FlsUtil.Point[]::new);

        Document doc = new Document();
        doc.put("type", "LineString");
        doc.put("coordinates", asList(polygon));

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
