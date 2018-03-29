package org.bson.codec;

import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.operator.Operator;
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
                    Operator op = OperatorFactory.create(key.substring(1), (Document) entry.getValue());
                    if (op != null) {
                        return op;
                    }
                }
            }
        }
        else if (objectToTransform instanceof String) {
            String value = (String) objectToTransform;
            if (value.startsWith("$")) {
                Operator op = OperatorFactory.create(value.substring(1));
                if (op != null) {
                    return op;
                }
            }
        }

        return objectToTransform;
    }
}
