module uk.dioxic.mgenerate.apt {
    exports uk.dioxic.mgenerate.apt.processor;
    requires java.compiler;
    requires uk.dioxic.mgenerate.common;
    requires org.mongodb.bson;
    requires faker4j;
    requires com.squareup.javapoet;
}