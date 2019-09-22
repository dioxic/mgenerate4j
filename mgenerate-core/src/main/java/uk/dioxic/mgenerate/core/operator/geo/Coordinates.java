package uk.dioxic.mgenerate.core.operator.geo;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.util.List;

import static java.util.Arrays.asList;

@Operator({"coordinates", "coord"})
public class Coordinates extends AbstractOperator<uk.dioxic.mgenerate.core.operator.type.Coordinates> {

    @OperatorProperty
    List<Number> longBounds = asList(-180d, 180d);

    @OperatorProperty
    List<Number> latBounds = asList(-90d, 90d);

    @Override
    public uk.dioxic.mgenerate.core.operator.type.Coordinates resolveInternal() {
        double longitude = FakerUtil.randomDouble(longBounds.get(0), longBounds.get(1));
        double latitude = FakerUtil.randomDouble(latBounds.get(0), latBounds.get(1));
        return new uk.dioxic.mgenerate.core.operator.type.Coordinates(longitude, latitude);
    }

}
