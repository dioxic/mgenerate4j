package uk.dioxic.mgenerate;

import org.bson.Document;
import uk.dioxic.faker.resolvable.Resolvable;

public interface ResolvableBuilder<T extends Resolvable> {

    T build();

    ResolvableBuilder<T> document(Document document);

    static <T> Resolvable<T> wrap(Document document, String key, Class<T> desiredType) {
        return OperatorFactory.wrap(document.get(key), desiredType);
    }

}
