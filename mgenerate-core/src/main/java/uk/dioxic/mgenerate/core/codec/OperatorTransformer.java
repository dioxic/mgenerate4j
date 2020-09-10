package uk.dioxic.mgenerate.core.codec;

import org.bson.Document;
import org.bson.Transformer;
import uk.dioxic.mgenerate.core.VariableCache;
import uk.dioxic.mgenerate.core.operator.OperatorFactory;
import uk.dioxic.mgenerate.core.resolver.PatternResolver;
import uk.dioxic.mgenerate.core.resolver.VariableResolver;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.util.Map;

public class OperatorTransformer implements Transformer {
    @Override
    public Object transform(Object objectToTransform) {

        if (objectToTransform instanceof Document) {
            Document doc = (Document) objectToTransform;
            if (doc.size() == 1) {
                Map.Entry<String, Object> entry = doc.entrySet().iterator().next();
                String key = entry.getKey();
                if (OperatorFactory.canHandle(key)) {
                    return OperatorFactory.create(key, entry.getValue());
                }
            }
        } else if (objectToTransform instanceof String) {
            String value = (String) objectToTransform;
            if (OperatorFactory.canHandle(value)) {
                return OperatorFactory.create(value);
            } else if (PatternResolver.canHandle(value)) {
                return new PatternResolver(value, FakerUtil.instance());
            } else if (VariableCache.canHandle(value)) {
                return new VariableResolver(value);
            }
        }

        return objectToTransform;
    }
}
