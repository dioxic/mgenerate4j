package uk.dioxic.mgenerate.apt.poet;

import com.squareup.javapoet.*;
import org.bson.Document;
import org.bson.assertions.Assertions;
import uk.dioxic.mgenerate.apt.model.AbstractFieldModel;
import uk.dioxic.mgenerate.apt.model.OperatorPropertyModel;
import uk.dioxic.mgenerate.apt.util.ModelUtil;
import uk.dioxic.mgenerate.apt.util.StringUtil;
import uk.dioxic.mgenerate.common.*;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorBuilder;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static uk.dioxic.mgenerate.apt.util.ModelUtil.isSameType;

public class OperatorBuilderPoet implements Poet {

    private final static String TRANSFORMER_REGISTRY = "transformerRegistry";
    private final TypeElement typeElement;
    private final String packageName;
    private final String className;
    private final ClassName thisType;

    public OperatorBuilderPoet(TypeElement typeElement) {
        this.typeElement = typeElement;
        this.packageName = ModelUtil.elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        this.className = typeElement.getSimpleName() + "Builder";
        this.thisType = ClassName.get(typeElement);
    }

    @Override
    public CharSequence getFullyQualifiedName() {
        if (this.packageName != null && this.packageName.trim().length() > 0) {
            return this.packageName + "." + this.className;
        }
        return this.className;
    }

    @Override
    public void generate(Appendable appendable) throws IOException {
        ClassName builderInterface = ClassName.get(ResolvableBuilder.class);
        List<OperatorPropertyModel> properties = getProperties();

        AnnotationSpec.Builder annotationBuilder = AnnotationSpec.builder(OperatorBuilder.class);

        for (String key : getOperatorKeys()) {
            annotationBuilder.addMember("value", "$S", key);
        }

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(ParameterizedTypeName.get(builderInterface, this.thisType))
                .addAnnotation(annotationBuilder.build());

        addConstructor(classBuilder);

        addProperties(classBuilder, properties);

        addPropertyMethods(classBuilder, properties);

        addDocumentMethod(classBuilder, properties);

        addSingleValueMethod(classBuilder, properties);

        MethodSpec validateMethod = addValidateMethod(classBuilder, properties);

        addBuildMethod(classBuilder, properties, validateMethod);

        TypeSpec classSpec = classBuilder.build();

        JavaFile javaFile = JavaFile.builder(this.packageName, classSpec).build();
        javaFile.writeTo(appendable);
    }

    private void addConstructor(TypeSpec.Builder classBuilder) {
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get(TransformerRegistry.class), TRANSFORMER_REGISTRY)
                .addStatement("this.$L = $L", TRANSFORMER_REGISTRY, TRANSFORMER_REGISTRY)
                .build();

        classBuilder.addMethod(constructor);
    }

    private void addProperties(TypeSpec.Builder classBuilder, List<OperatorPropertyModel> properties) {
        classBuilder.addField(ClassName.get(TransformerRegistry.class), TRANSFORMER_REGISTRY, Modifier.PRIVATE, Modifier.FINAL);
        for (OperatorPropertyModel field : properties) {
            classBuilder.addField(field.getResolvableTypeName(), field.getName(), Modifier.PRIVATE);
        }
    }

    private List<OperatorPropertyModel> getProperties() {
        List<Element> elements = new ArrayList<>();

        TypeMirror superClass = typeElement.getSuperclass();

        if (!"java.lang.Object".equals(superClass.toString())) {
            elements.addAll(((DeclaredType) superClass).asElement().getEnclosedElements());
        }

        elements.addAll(typeElement.getEnclosedElements());

        return elements.stream().filter(this::filterOperatorProperties)
                .map(OperatorPropertyModel::new)
                .collect(Collectors.toList());
    }

    private boolean filterOperatorProperties(Element el) {
        return (el.getKind() == ElementKind.FIELD
                || el.getKind() == ElementKind.METHOD)
                && !el.getModifiers().contains(Modifier.PRIVATE)
                && el.getAnnotation(OperatorProperty.class) != null;
    }

    private void addBuildMethod(TypeSpec.Builder classBuilder, List<OperatorPropertyModel> properties, MethodSpec validate) {

        CodeBlock.Builder requiredBlock = CodeBlock.builder();

        for (OperatorPropertyModel property : properties) {
            if (!property.isRequired()) {
                requiredBlock.beginControlFlow("if ($L != null)", property.getName());
            }

            if (!property.isResolvableType()) {
                if (property.isMethod()) {
                    requiredBlock.addStatement("obj.$L($T.recursiveResolve($L))",
                            property.getMethodName(),
                            Resolvable.class,
                            property.getName());
                }
                else {
                    requiredBlock.addStatement("obj.$L = $T.recursiveResolve($L)",
                            property.getName(),
                            Resolvable.class,
                            property.getName());
                }
            } else {
                if (property.isMethod()) {
                    requiredBlock.addStatement("obj.$L($L)", property.getMethodName(), property.getName());
                }
                else {
                    requiredBlock.addStatement("obj.$L = $L", property.getName(), property.getName());
                }
            }

            if (!property.isRequired()) {
                requiredBlock.endControlFlow();
            }
        }

        CodeBlock.Builder initBlock = CodeBlock.builder();

        if (typeElement.getInterfaces().stream().anyMatch(clazz -> isSameType(clazz, Initializable.class))) {
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

    private MethodSpec addValidateMethod(TypeSpec.Builder classBuilder, List<OperatorPropertyModel> properties) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("validate")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(void.class);

        properties.stream().filter(OperatorPropertyModel::isRequired).forEach(o ->
                builder.addStatement("$T.notNull($S,$L)", Assertions.class, o.getName(), o.getName())
        );

        MethodSpec method = builder.build();
        classBuilder.addMethod(method);
        return method;
    }

    private void addDocumentMethod(TypeSpec.Builder classBuilder, List<OperatorPropertyModel> properties) {
        AnnotationSpec suppressUnchecked = AnnotationSpec.builder(SuppressWarnings.class)
                .addMember("value", "$S", "unchecked")
                .build();

        MethodSpec.Builder builder = MethodSpec.methodBuilder("document")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(Override.class)
                .addAnnotation(suppressUnchecked)
                .addParameter(Document.class, "document")
                .returns(ClassName.get(packageName, className));

        for (OperatorPropertyModel property : properties) {
            if (property.isRootTypeNameParameterized()) {
                builder.addStatement("this.$L = ($T)$T.wrap(document.get($S),$T.class,$L)", property.getName(), Resolvable.class, Wrapper.class, property.getName(), property.getRootTypeNameErasure(), TRANSFORMER_REGISTRY);
            } else {
                builder.addStatement("this.$L = $T.wrap(document.get($S),$T.class,$L)", property.getName(), Wrapper.class, property.getName(), property.getRootTypeName(), TRANSFORMER_REGISTRY);
            }
        }

        builder.addStatement("return this");

        classBuilder.addMethod(builder.build());
    }

    private void addSingleValueMethod(TypeSpec.Builder classBuilder, List<OperatorPropertyModel> properties) {
        AnnotationSpec suppressUnchecked = AnnotationSpec.builder(SuppressWarnings.class)
                .addMember("value", "$S", "unchecked")
                .build();

        MethodSpec.Builder builder = MethodSpec.methodBuilder("singleValue")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(Override.class)
                .addAnnotation(suppressUnchecked)
                .addParameter(Object.class, "value")
                .returns(ClassName.get(packageName, className));

        long concretePropCount = properties.stream().filter(AbstractFieldModel::isFromConcreteClass).count();

        for (OperatorPropertyModel property : properties) {
            if (!property.isFromSuperClass() && (property.isPrimary() || property.isRequired() || concretePropCount == 1)) {
                if (property.isRootTypeNameParameterized()) {
                    builder.addStatement("this.$L = ($T)$T.wrap(value,$T.class,$L)", property.getName(), Resolvable.class, Wrapper.class, property.getRootTypeNameErasure(), TRANSFORMER_REGISTRY);
                } else {
                    builder.addStatement("this.$L = $T.wrap(value,$T.class,$L)", property.getName(), Wrapper.class, property.getRootTypeName(), TRANSFORMER_REGISTRY);
                }
            }
        }

        builder.addStatement("return this");

        classBuilder.addMethod(builder.build());
    }

    private void addPropertyMethods(TypeSpec.Builder classBuilder, List<OperatorPropertyModel> properties) {
        for (OperatorPropertyModel property : properties) {
            classBuilder.addMethod(MethodSpec.methodBuilder(property.getName())
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .returns(ClassName.get(packageName, className))
                    .addParameter(property.getResolvableTypeName(), property.getName())
                    .addStatement("this.$L = $L", property.getName(), property.getName())
                    .addStatement("return this")
                    .build());

            classBuilder.addMethod(MethodSpec.methodBuilder(property.getName())
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .returns(ClassName.get(packageName, className))
                    .addParameter(property.getRootTypeName(TypeKind.WILDCARD), property.getName())
                    .addStatement("this.$L = $T.wrap($L)", property.getName(), Wrapper.class, property.getName())
                    .addStatement("return this")
                    .build());

        }
    }

}
