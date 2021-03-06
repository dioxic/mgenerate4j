package uk.dioxic.mgenerate.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.mgenerate.common.annotation.ValueTransformer;

import java.util.HashMap;
import java.util.Map;

public class TransformerRegistry {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<Class, Transformer> transformerMap = new HashMap<>();

    public void addTransformer(Transformer transformer) {
        ValueTransformer annotation = transformer.getClass().getAnnotation(ValueTransformer.class);
        logger.debug("adding [{}] to transformer registry", annotation.value());
        transformerMap.put(annotation.value(), transformer);
    }

    @SuppressWarnings("unchecked")
    public <T> Transformer<T> get(Class<T> desiredClass) {
        return (Transformer<T>) transformerMap.get(desiredClass);
    }

    public boolean canHandle(Class desiredClass) {
        return transformerMap.containsKey(desiredClass);
    }

}