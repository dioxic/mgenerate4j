package com.dioxic.mgenerate.operator;

import java.util.ArrayList;
import java.util.List;

import com.dioxic.mgenerate.annotation.BuilderProperty;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.Document;

import com.dioxic.mgenerate.FakerUtil;

@OperatorClass
public class Choose implements Operator {

    @OperatorProperty(required = true)
	private Operator from;

    @OperatorProperty
	private Operator weights;

	@Override
	public Object resolve() {
		Object[] from = Operator.resolveArray(this.from);

		if (this.weights != null) {
			Integer[] weights = Operator.resolveIntArray(this.weights);
			if (from.length != weights.length) {
				throw new IllegalArgumentException("length of array and weights must match");
			}
			List<Object> fromList = new ArrayList<>();
			for (int i = 0; i < from.length; i++) {
				for (int j = 0; j < weights[i]; j++) {
					fromList.add(from[i]);
				}
			}
			from = fromList.toArray();
		}

		Object result = from[FakerUtil.instance().random().nextInt(from.length)];

		return result instanceof Operator ? ((Operator)result).resolve() : result;

	}

}
