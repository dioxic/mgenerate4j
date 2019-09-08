package uk.dioxic.mgenerate.core.operator;

import org.bson.Document;
import uk.dioxic.mgenerate.common.Cache;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

@Operator
public class Choose implements Resolvable<Object> {

    @OperatorProperty(required = true)
    Resolvable<List<?>> from;

    @OperatorProperty
    Resolvable<List<Integer>> weights;

    @Override
    public Object resolve(Cache cache) {
        List<?> from = this.from.resolve(cache);

        if (this.weights != null) {
            List<Integer> weights = this.weights.resolve(cache);
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

        Object result = from.get(FakerUtil.random().nextInt(from.size()));

        return Resolvable.rescursiveResolve(result, cache);
    }


    @Override
    public Object resolve() {
        return resolve(null);
    }

}
