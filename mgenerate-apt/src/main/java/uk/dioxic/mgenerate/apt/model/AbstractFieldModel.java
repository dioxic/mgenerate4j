package uk.dioxic.mgenerate.apt.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import uk.dioxic.mgenerate.common.Resolvable;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.time.LocalDateTime;
import java.util.List;

import static uk.dioxic.mgenerate.apt.util.ModelUtil.*;

public abstract class AbstractFieldModel {

    private final String name;
    private TypeMirror type;
    private List<? extends TypeMirror> typeParameters;
    private String methodName;
    private boolean fromSuperClass;

    public AbstractFieldModel(String name, TypeMirror type) {

        if (type.getKind() == TypeKind.EXECUTABLE) {
            List<? extends TypeMirror> parms = ((ExecutableType)type).getParameterTypes();
            if (parms.size() != 1) {
                throw new IllegalStateException("Operator property methods must have a single argment only");
            }
            methodName = name;
            fromSuperClass = true;
            name = removeSetPrefix(name);
            type = ((ExecutableType)type).getParameterTypes().get(0);
        }

        if (type.getKind() == TypeKind.DECLARED) {
            this.type = type;
            typeParameters = ((DeclaredType) type).getTypeArguments();
        }
        else {
            throw new IllegalStateException("Cannot process " + name);
        }

        this.name = name;
    }

    private String removeSetPrefix(String s) {
        if (s.startsWith("set")) {
            s = s.substring("set".length());
            s = Character.toLowerCase(s.charAt(0)) + s.substring(1);
        }
        return s;
    }

    public String getName() {
        return name;
    }

    public String getMethodName() {
        return methodName;
    }

    public boolean isFromSuperClass() {
        return fromSuperClass;
    }

    public boolean isFromConcreteClass() {
        return !fromSuperClass;
    }

    public boolean isMethod() {
        return methodName != null;
    }

    public TypeMirror getType() {
        return type;
    }

    public boolean isResolvableType() {
        return isSameType(type, Resolvable.class);
    }

    public TypeName getResolvableTypeName() {
        if (isResolvableType()) {
            return ParameterizedTypeName.get(ClassName.get(Resolvable.class), getRootTypeName());
        }
        else {
            return ParameterizedTypeName.get(ClassName.get(Resolvable.class), TypeName.get(type));
        }
    }

    public TypeName getRootTypeName() {
        if (isResolvableType()) {
            if (!typeParameters.isEmpty()) {
                return ClassName.get(typeParameters.get(0));
            }
            return ClassName.get(Object.class);
        }
        return TypeName.get(type);
    }

    public TypeName getRootTypeNameErasure() {
        if (isResolvableType()) {
            if (!typeParameters.isEmpty()) {
                return ClassName.get(erasure(typeParameters.get(0)));
            }
            return ClassName.get(Object.class);
        }
        return TypeName.get(erasure(type));
    }

    public boolean isRootTypeNameParameterized() {
        return !getRootTypeName().equals(getRootTypeNameErasure());
    }

    public boolean isEnumRootType() {
        if (isResolvableType()) {
            return !typeParameters.isEmpty() && isEnum(typeParameters.get(0));
        }
        return isEnum(type);
    }

    public boolean isDateRootType() {
        if (isResolvableType()) {
            return !typeParameters.isEmpty() && isSameType(typeParameters.get(0), LocalDateTime.class);
        }
        return isSameType(type, LocalDateTime.class);
    }
}
