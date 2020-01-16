package uk.dioxic.mgenerate.core.operator.type;

import java.util.UUID;

public enum UuidType {
        STRING,
        BINARY;

        public Object toOutputType(UUID uuid) {
            switch (this) {
                case STRING:
                    return uuid.toString();
                default:
                    return uuid;
            }
        }
    }