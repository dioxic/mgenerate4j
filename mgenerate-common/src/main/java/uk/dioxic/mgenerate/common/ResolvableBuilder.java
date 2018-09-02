package uk.dioxic.mgenerate.common;

import org.bson.Document;

public interface ResolvableBuilder<T extends Resolvable> {

    T build();

    ResolvableBuilder<T> document(Document document);

}
