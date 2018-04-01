package com.dioxic.mgenerate;

import org.bson.Document;

public interface ResolvableBuilder<T extends Resolvable> {

    T build();

    ResolvableBuilder<T> document(Document document);

}
