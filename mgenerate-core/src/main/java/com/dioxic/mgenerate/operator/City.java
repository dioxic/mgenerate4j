package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.annotation.OperatorClass;
import org.bson.types.MinKey;

@OperatorClass
public class City implements Operator {

	@Override
	public MinKey resolve() {
		return FakerUtil.instance().address().city();
	}

}
