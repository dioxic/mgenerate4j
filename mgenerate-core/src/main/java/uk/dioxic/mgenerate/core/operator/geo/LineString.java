package uk.dioxic.mgenerate.core.operator.geo;

import org.bson.Document;
import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

@Operator
public class LineString extends AbstractOperator<Document> implements Initializable {

    @OperatorProperty
    List<Number> longBounds = asList(-180d, 180d);

    @OperatorProperty
    List<Number> latBounds = asList(-90d, 90d);

    @OperatorProperty
    Resolvable<Integer> locs = Wrapper.wrap(2);

    private Coordinates coordinates;

    @Override
    public Document resolveInternal() {
        uk.dioxic.mgenerate.core.operator.type.Coordinates[] lineString = Stream.generate(coordinates::resolveInternal)
                .limit(locs.resolve())
                .toArray(uk.dioxic.mgenerate.core.operator.type.Coordinates[]::new);

        Document doc = new Document();
        doc.put("type", "LineString");
        doc.put("coordinates", asList(lineString));

        return doc;
    }

    @Override
    public void initialize() {
        coordinates = new CoordinatesBuilder(ReflectiveTransformerRegistry.getInstance())
                .longBounds(longBounds)
                .latBounds(latBounds)
                .build();
    }
}
