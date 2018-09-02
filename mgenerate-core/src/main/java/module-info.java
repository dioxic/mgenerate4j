module uk.dioxic.mgenerate.core {
    exports uk.dioxic.mgenerate.core;
    exports uk.dioxic.mgenerate.core.codec;
    requires org.mongodb.bson;
    requires commons.cli;
    requires uk.dioxic.mgenerate.common;
    requires uk.dioxic.faker4j;
    requires org.apache.logging.log4j;
    requires slf4j.api;
    requires bsoncodec;
    requires guava;
    requires org.apache.commons.codec;
    requires reflections;
}