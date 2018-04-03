package com.dioxic.mgenerate;

import org.bson.Document;
import uk.dioxic.faker.resolvable.Resolvable;

public interface ResolvableBuilder<T extends Resolvable> {

    T build();

    ResolvableBuilder<T> document(Document document);

}
