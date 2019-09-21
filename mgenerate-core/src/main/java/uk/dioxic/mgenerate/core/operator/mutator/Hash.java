package uk.dioxic.mgenerate.core.operator.mutator;

import org.bson.Document;
import org.bson.codecs.DocumentCodec;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.codec.TemplateCodec;
import uk.dioxic.mgenerate.core.operator.type.HashType;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Operator
public class Hash implements Resolvable<Object> {

    @OperatorProperty(required = true)
    Resolvable<Object> input;

    @OperatorProperty
    String algorithm = "MD5";

    @OperatorProperty
    HashType output = HashType.INT32;

    @Override
    public Object resolve() {
        Object value = input.resolve();
        byte[] valBytes;

        if (value instanceof Document) {
            valBytes = ((Document) value).toJson(new DocumentCodec(TemplateCodec.getCodeRegistry())).getBytes();
        } else {
            valBytes = value.toString().getBytes();
        }

        try {
            return output.toOutputType(MessageDigest.getInstance(algorithm).digest(valBytes));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
