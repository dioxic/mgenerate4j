package uk.dioxic.mgenerate.core.operator.geo;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.util.List;

import static java.util.Arrays.asList;

@Operator({"coordinates", "coordinate", "coord"})
public class Coordinates implements Resolvable<uk.dioxic.mgenerate.core.operator.type.Coordinates> {

    @OperatorProperty
    List<Number> long_lim = asList(-180d, 180d);

    @OperatorProperty
    List<Number> lat_lim = asList(-90d, 90d);

    @Override
    public uk.dioxic.mgenerate.core.operator.type.Coordinates resolve() {
        double longitude = FakerUtil.randomDouble(long_lim.get(0), long_lim.get(1));
        double latitude = FakerUtil.randomDouble(lat_lim.get(0), lat_lim.get(1));
        return new uk.dioxic.mgenerate.core.operator.type.Coordinates(longitude, latitude);
    }

}
