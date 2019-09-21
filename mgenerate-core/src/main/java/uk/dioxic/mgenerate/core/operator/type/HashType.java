package uk.dioxic.mgenerate.core.operator.type;

import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;

public enum HashType {
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