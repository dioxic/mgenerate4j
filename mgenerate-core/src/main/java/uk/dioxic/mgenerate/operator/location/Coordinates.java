package uk.dioxic.mgenerate.operator.location;

import uk.dioxic.faker.resolvable.Resolvable;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.mgenerate.util.FakerUtil;
import uk.dioxic.mgenerate.util.FlsUtil;

import java.util.List;

import static java.util.Arrays.asList;

@Operator({"coordinates", "coordinate", "coord"})
public class Coordinates implements Resolvable<FlsUtil.Point> {

    @OperatorProperty
    List<Number> long_lim = asList(-180d, 180d);

    @OperatorProperty
    List<Number> lat_lim = asList(-90d, 90d);

    @Override
    public FlsUtil.Point resolve() {
        double longitude = FakerUtil.randomDouble(long_lim.get(0), long_lim.get(1));
        double latitude = FakerUtil.randomDouble(lat_lim.get(0), lat_lim.get(1));
        return new FlsUtil.Point(longitude, latitude);
    }
}
