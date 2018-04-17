package uk.dioxic.mgenerate.apt.processor;

import com.squareup.javapoet.*;
import org.bson.Document;
import org.bson.assertions.Assertions;
import uk.dioxic.faker.resolvable.Resolvable;
import uk.dioxic.mgenerate.Initializable;
import uk.dioxic.mgenerate.OperatorFactory;
import uk.dioxic.mgenerate.ResolvableBuilder;
import uk.dioxic.mgenerate.StringUtil;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorBuilder;
import uk.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.mgenerate.apt.model.FieldModel;
import uk.dioxic.mgenerate.operator.Wrapper;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

class BuilderGenerator {

    private final TypeElement typeElement;
    private final String packageName;
    private final String className;
    private final ClassName thisType;

    BuilderGenerator(TypeElement typeElement) {
        this.typeElement = typeElement;
        this.packageName = Util.elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        this.className = typeElement.getSimpleName() + "Builder";
        this.thisType = ClassName.get(typeElement);
    }

    CharSequence getFullyQualifiedName() {
        if (this.packageName != null && this.packageName.trim().length() > 0) {
            return this.packageName + "." + this.className;
        }
        return this.className;
    }

    void generate(Appendable appendable) throws IOException {
        ClassName builderInterface = ClassName.get(ResolvableBuilder.class);
        List<FieldModel> properties = getProperties();

        AnnotationSpec.Builder annotationBuilder = AnnotationSpec.builder(OperatorBuilder.class);

        for (String key : getOperatorKeys()) {
            annotationBuilder.addMember("value", "$S", key);
        }

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(ParameterizedTypeName.get(builderInterface, this.thisType))
                .addAnnotation(annotationBuilder.build());

        addFields(classBuilder, properties);

        addPropertyMethods(classBuilder, properties);

        addDocumentMethod(classBuilder, properties);

        MethodSpec validateMethod = addValidateMethod(classBuilder, properties);

        addBuildMethod(classBuilder, properties, validateMethod);

        TypeSpec classSpec = classBuilder.build();

        JavaFile javaFile = JavaFile.builder(this.packageName, classSpec).build();
        javaFile.writeTo(appendable);
    }

    private void addFields(TypeSpec.Builder classBuilder, List<FieldModel> properties) {
        for (FieldModel field : properties) {
            classBuilder.addField(field.getOperatorTypeName(), field.getName(), Modifier.PRIVATE);
        }
    }

    private List<FieldModel> getProperties() {
        return typeElement
                .getEnclosedElements().stream().filter(this::filterOperatorProperties)
                .map(FieldModel::new)
                .collect(Collectors.toList());
    }

    private boolean filterOperatorProperties(Element el) {
        return el.getKind() == ElementKind.FIELD
                && !el.getModifiers().contains(Modifier.PRIVATE)
                && el.getAnnotation(OperatorProperty.class) != null;
    }

    private void addBuildMethod(TypeSpec.Builder classBuilder, List<FieldModel> properties, MethodSpec validate) {

        CodeBlock.Builder requiredBlock = CodeBlock.builder();

        for (FieldModel property : properties) {
            if (!property.isRequired()) {
                requiredBlock.beginControlFlow("if ($L != null)", property.getName());
            }

            if (!property.isOperatorType()) {
                requiredBlock.addStatement("obj.$L = $L.resolve()",
                        property.getName(),
                        property.getName());
            }
            else {
                requiredBlock.addStatement("obj.$L = $L", property.getName(), property.getName());
            }

            if (!property.isRequired()) {
                requiredBlock.endControlFlow();
            }
        }

        CodeBlock.Builder initBlock = CodeBlock.builder();

        if (typeElement.getInterfaces().stream().anyMatch(clazz -> Util.isSameType(clazz, Initializable.class))) {
            initBlock.addStatement("obj.initialize()");
        }

        MethodSpec method = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(this.thisType)
                .addAnnotation(Override.class)
                .addStatement("$N()", validate)
                .addStatement("$T obj = new $T()", thisType, thisType)
                .addCode(requiredBlock.build())
                .addCode(initBlock.build())
                .addStatement("return obj")
                .build();

        classBuilder.addMethod(method);
    }

    private String[] getOperatorKeys() {
        String[] operatorKeys = typeElement.getAnnotation(Operator.class).value();

        if (operatorKeys.length == 1 && operatorKeys[0].isEmpty()) {
            return new String[]{StringUtil.lowerCaseFirstLetter(typeElement.getSimpleName().toString())};
        }

        return operatorKeys;
    }

    private MethodSpec addValidateMethod(TypeSpec.Builder classBuilder, List<FieldModel> properties) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("validate")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(void.class);

        properties.stream().filter(FieldModel::isRequired).forEach(o ->
                builder.addStatement("$T.notNull($S,$L)", Assertions.class, o.getName(), o.getName())
        );

        MethodSpec method = builder.build();
        classBuilder.addMethod(method);
        return method;
    }

    private void addDocumentMethod(TypeSpec.Builder classBuilder, List<FieldModel> properties) {
        AnnotationSpec suppressUnchecked = AnnotationSpec.builder(SuppressWarnings.class)
                .addMember("value", "$S", "unchecked")
                .build();

        MethodSpec.Builder builder = MethodSpec.methodBuilder("document")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(Override.class)
                .addAnnotation(suppressUnchecked)
                .addParameter(Document.class, "document")
                .returns(ClassName.get(packageName, className));

        for (FieldModel property : properties) {
            if (property.isRootTypeNameParameterized()) {
                builder.addStatement("$L = ($T)$T.wrap(document.get($S),$T.class)", property.getName(), Resolvable.class, OperatorFactory.class, property.getName(), property.getRootTypeNameErasure());
            }
            else {
                builder.addStatement("$L = $T.wrap(document.get($S),$T.class)", property.getName(), OperatorFactory.class, property.getName(), property.getRootTypeName());
            }
        }

        builder.addStatement("return this");

        classBuilder.addMethod(builder.build());
    }

    private void addPropertyMethods(TypeSpec.Builder classBuilder, List<FieldModel> properties) {
        for (FieldModel property : properties) {
            classBuilder.addMethod(MethodSpec.methodBuilder(property.getName())
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .returns(ClassName.get(packageName, className))
                    .addParameter(property.getOperatorTypeName(), property.getName())
                    .addStatement("this.$L = $L", property.getName(), property.getName())
                    .addStatement("return this")
                    .build());

            classBuilder.addMethod(MethodSpec.methodBuilder(property.getName())
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .returns(ClassName.get(packageName, className))
                    .addParameter(property.getRootTypeName(), property.getName())
                    .addStatement("this.$L = new $T<>($L)", property.getName(), Wrapper.class, property.getName())
                    .addStatement("return this")
                    .build());

        }
    }

}
