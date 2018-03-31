package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;

import java.util.ArrayList;
import java.util.List;

@OperatorClass
public class Choose implements Resolvable<Object> {

    @OperatorProperty(required = true)
    Resolvable<List<?>> from;

    @OperatorProperty
    Resolvable<List<Integer>> weights;

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

		Object result = from.get(FakerUtil.instance().random().nextInt(from.size()));

		return result instanceof Resolvable ? ((Resolvable)result).resolve() : result;

	}

}
