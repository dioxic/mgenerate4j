package uk.dioxic.mgenerate.core.operator.geo;

import org.bson.Document;
import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.List;

import static java.util.Arrays.asList;

@Operator
public class Point extends AbstractOperator<Document> implements Initializable {

    @OperatorProperty
    List<Number> longBounds = asList(-180d, 180d);

    @OperatorProperty
    List<Number> latBounds = asList(-90d, 90d);

    private Coordinates coordinates;

    @Override
    public Document resolveInternal() {
        Document doc = new Document();
        doc.put("type", "Point");
        doc.put("coordinates", coordinates.resolveInternal());

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
