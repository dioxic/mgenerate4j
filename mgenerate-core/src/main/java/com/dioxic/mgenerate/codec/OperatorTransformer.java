package com.dioxic.mgenerate.codec;

import com.dioxic.mgenerate.OperatorFactory;
import org.bson.Document;
import org.bson.Transformer;

import java.util.Map;

public class OperatorTransformer implements Transformer {
    @Override
    public Object transform(Object objectToTransform) {

        if (objectToTransform instanceof Document) {
            Document doc = (Document) objectToTransform;
            if (doc.size() == 1) {
                Map.Entry<String, Object> entry = doc.entrySet().iterator().next();
                String key = entry.getKey();
                if (entry.getValue() instanceof Document && key.startsWith("$")) {
                    key = key.substring(1);
                    if (OperatorFactory.contains(key)) {
                        return OperatorFactory.create(key, (Document) entry.getValue());
                    }
                }
            }
        }
        else if (objectToTransform instanceof String) {
            String value = (String) objectToTransform;
            if (value.startsWith("$")) {
                value = value.substring(1);
                if (OperatorFactory.contains(value)) {
                    return OperatorFactory.create(value);
                }
            }
        }

        return objectToTransform;
    }
}
