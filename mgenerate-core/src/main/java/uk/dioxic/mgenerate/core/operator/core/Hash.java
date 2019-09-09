package uk.dioxic.mgenerate.core.operator.core;

import org.apache.commons.codec.binary.Hex;
import org.bson.Document;
import org.bson.codecs.DocumentCodec;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.codec.TemplateCodec;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Operator
public class Hash implements Resolvable<Object> {

    @OperatorProperty(required = true)
    Resolvable<Object> input;

    @OperatorProperty
    String algorithm = "MD5";

    @OperatorProperty
    HashOutput output = HashOutput.INT32;

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

    public enum HashOutput {
        INT32,
        INT64,
        HEX;

        public Object toOutputType(byte[] bytes) {
            switch (this) {
                case HEX:
                    return Hex.encodeHexString(bytes);
                case INT64:
                    return ByteBuffer.wrap(bytes).getLong();
                default:
                    return ByteBuffer.wrap(bytes).getInt();
            }
        }
    }
}
