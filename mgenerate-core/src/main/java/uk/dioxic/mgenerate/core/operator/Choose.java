package uk.dioxic.mgenerate.core.operator;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.util.ArrayList;
import java.util.List;

@Operator
public class Choose implements Resolvable<Object> {

    @OperatorProperty(required = true)
    Resolvable<List<?>> from;

    @OperatorProperty
    Resolvable<List<Integer>> weights;

	Choose() {
	}

	@Override
	public Object resolve() {
		List<?> from = this.from.resolve();

		if (this.weights != null) {
			List<Integer> weights = this.weights.resolve();
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

		return result instanceof Resolvable ? ((Resolvable)result).resolve() : result;

	}

}
