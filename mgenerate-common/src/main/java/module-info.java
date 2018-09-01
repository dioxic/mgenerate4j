module uk.dioxic.mgenerate.common {
    exports uk.dioxic.mgenerate.common;
    exports uk.dioxic.mgenerate.common.annotation;
    exports uk.dioxic.mgenerate.common.exception;
    exports uk.dioxic.mgenerate.common.operator;
    exports uk.dioxic.mgenerate.common.transformer;
    requires org.mongodb.bson;
    requires reflections;
    requires faker4j;
    requires slf4j.api;
    requires org.apache.logging.log4j;
}