module uk.dioxic.mgenerate.apt {
    exports uk.dioxic.mgenerate.apt.processor;
    requires java.compiler;
    requires uk.dioxic.mgenerate.common;
    requires org.mongodb.bson;
    requires com.squareup.javapoet;
}