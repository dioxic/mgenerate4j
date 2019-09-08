package uk.dioxic.mgenerate.core.operator;

import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.State;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.util.ArrayList;
import java.util.List;

@Operator
public class Choose implements Resolvable<Object>, Initializable {

    @OperatorProperty(required = true)
    List<?> from;

    @OperatorProperty
    List<Integer> weights;

    @Override
    public Object resolve() {
        return from.get(FakerUtil.random().nextInt(from.size()));
    }

    @Override
    public void initialize() {
        if (this.weights != null) {
            if (from.size() != weights.size()) {
                throw new IllegalArgumentException("length of array and weights must match");
            }
            List<Object> fromList = new ArrayList<>();
            for (int i = 0; i < from.size(); i++) {
                for (int j = 0; j < weights.get(i); j++) {
                    fromList.add(from.get(i));
                }
            }
            from = fromList;
        }
    }
}
