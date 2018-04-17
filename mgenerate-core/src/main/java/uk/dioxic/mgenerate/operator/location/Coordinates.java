package uk.dioxic.mgenerate.operator.location;

import uk.dioxic.faker.resolvable.Resolvable;
import uk.dioxic.mgenerate.FakerUtil;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;

import java.util.List;

import static java.util.Arrays.asList;

@Operator()
public class Coordinates implements Resolvable<List>{

    @OperatorProperty
    List<Number> long_lim = asList(-180d, 180d);

    @OperatorProperty
    List<Number> lat_lim = asList(-90d, 90d);


    @Override
    public List<Double> resolve() {
        return asList(FakerUtil.randomDouble(long_lim.get(0),long_lim.get(1)), FakerUtil.randomDouble(lat_lim.get(0),lat_lim.get(1)));
    }
}
