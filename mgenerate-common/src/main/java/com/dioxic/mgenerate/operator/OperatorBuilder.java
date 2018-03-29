package com.dioxic.mgenerate.operator;

import org.bson.Document;

public interface OperatorBuilder<T extends Operator> {

    T build();

    OperatorBuilder<T> document(Document document);

}
