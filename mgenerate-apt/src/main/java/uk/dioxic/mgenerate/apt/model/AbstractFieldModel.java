package uk.dioxic.mgenerate.apt.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import uk.dioxic.mgenerate.common.Resolvable;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.time.LocalDateTime;
import java.util.List;

import static uk.dioxic.mgenerate.apt.util.ModelUtil.*;

public abstract class AbstractFieldModel {

    private final String name;
    private final TypeMirror type;
    private List<? extends TypeMirror> typeParameters;

    public AbstractFieldModel(String name, TypeMirror type) {
        this.name = name;
        this.type = type;

        if (type.getKind() == TypeKind.DECLARED) {
            typeParameters = ((DeclaredType) type).getTypeArguments();
        }
    }

    public String getName() {
        return name;
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
