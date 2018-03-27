package com.dioxic.mgenerate;

import com.dioxic.mgenerate.operator.*;
import org.bson.Document;

import java.util.Map;

public class OperatorFactory {

    public static Operator create(String operator) {
        return create(operator, null);
    }

    public static Operator create(String operator, Document doc) {
        if (doc != null) {
            for (Map.Entry<String, Object> entry : doc.entrySet()) {
                entry.setValue(wrap(entry.getValue()));
            }
        }

        if (operator.startsWith("$")) {
            operator = operator.substring(1);
        }

        switch (operator) {
            case "array":
                return new Array();
            case "choose":
                return new Choose();
            case "number":
                return new com.dioxic.mgenerate.operator.Number();
            case "objectid":
                return new ObjectId();
            case "street":
                return new Street();
            case "city":
                return new City();
            case "postal":
                return new Postal();
            case "email":
                return new Email();
            case "date":
                return new Date();
            default:
                return null;
        }
    }

    public static Operator wrap(Object object) {
        if (object instanceof Operator) {
            return (Operator) object;
        }
        return new Wrapper(object);
    }
}
