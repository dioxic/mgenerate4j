package com.dioxic.mgenerate;

import org.bson.Document;

public interface OperatorBuilder<T extends Resolvable> {

    T build();

    OperatorBuilder<T> document(Document document);

}
