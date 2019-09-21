package uk.dioxic.mgenerate.core.transformer;

import org.bson.Document;
import uk.dioxic.mgenerate.common.Transformer;
import uk.dioxic.mgenerate.common.annotation.ValueTransformer;
import uk.dioxic.mgenerate.common.exception.TransformerException;

import java.time.temporal.ChronoField;
import java.util.Locale;

@ValueTransformer(Locale.class)
public class LocaleTransformer implements Transformer<Locale> {
    @Override
    public Locale transform(Object objectToTransform) {
        if (objectToTransform instanceof Locale) {
            return (Locale) objectToTransform;
        }

        if (objectToTransform instanceof Document) {
            Document doc = (Document)objectToTransform;
            if (doc.containsKey("language")) {
                if (doc.containsKey("country")) {
                    return new Locale(doc.getString("language"), doc.getString("country"));
                }
                else {
                    return new Locale(doc.getString("language"));
                }
            }
        }

        throw new TransformerException(objectToTransform.getClass(), Locale.class);
    }
}