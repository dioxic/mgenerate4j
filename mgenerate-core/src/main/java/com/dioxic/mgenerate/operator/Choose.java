package com.dioxic.mgenerate.operator;

import java.util.ArrayList;
import java.util.List;

import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.Document;

import com.dioxic.mgenerate.FakerUtil;
import org.bson.types.MinKey;

import static org.bson.assertions.Assertions.notNull;

@OperatorClass
public class Choose implements Operator {

    @OperatorProperty(required = true)
	Operator from;

    @OperatorProperty
	Operator weights;

    public Choose() {

    }

    public Choose(Document document) {
        from = document.get("from", Operator.class);
        weights = document.get("weights", Operator.class);
        notNull("from", from);
    }

	@Override
	public MinKey resolve() {
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
