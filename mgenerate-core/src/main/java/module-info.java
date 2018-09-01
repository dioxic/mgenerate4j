module uk.dioxic.mgenerate.core {
    exports uk.dioxic.mgenerate.core;
    requires org.mongodb.bson;
    requires commons.cli;
    requires uk.dioxic.mgenerate.common;
    requires faker4j;
    requires org.apache.logging.log4j;
    requires slf4j.api;
    requires uk.dioxic.mgenerate.apt;
    requires bsoncodec;
    requires guava;
    requires org.apache.commons.codec;
}