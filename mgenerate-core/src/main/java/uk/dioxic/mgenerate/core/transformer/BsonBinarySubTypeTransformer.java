package uk.dioxic.mgenerate.core.transformer;

import org.bson.BsonBinarySubType;
import uk.dioxic.mgenerate.common.Transformer;
import uk.dioxic.mgenerate.common.annotation.ValueTransformer;
import uk.dioxic.mgenerate.common.exception.TransformerException;
import uk.dioxic.mgenerate.core.operator.type.AgeType;

import java.util.Arrays;

@ValueTransformer(AgeType.class)
public class BsonBinarySubTypeTransformer implements Transformer<BsonBinarySubType> {
    @Override
    public BsonBinarySubType transform(Object objectToTransform) {
        if (objectToTransform instanceof Number) {
            return Arrays.stream(BsonBinarySubType.values())
                    .filter(subtype -> ((Number) objectToTransform).byteValue() == subtype.getValue())
                    .findFirst()
                    .orElseThrow(() -> new TransformerException(objectToTransform + " is not a valid BsonBinary subtype"));
        }

        if (objectToTransform instanceof String) {
            return BsonBinarySubType.valueOf(((String) objectToTransform).toUpperCase());
        }

        throw new TransformerException(objectToTransform.getClass(), AgeType.class);
    }

}