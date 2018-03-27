package com.dioxic.mgenerate.annotation.processor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Types;

public class AnnotationHierarchyUtil {

	private final Types types;

	public AnnotationHierarchyUtil(Types types) {
		this.types = types;
	}

	public Set<TypeElement> filterTriggeringAnnotations(
			Set<? extends TypeElement> aAnnotations, TypeElement bsonDocumentAnnotation) {
		Set<TypeElement> result = new HashSet<>();
		for (TypeElement annoEl : aAnnotations) {
			Set<TypeElement> hierarchy = getAnnotationHierarchy(annoEl);
			if (containsAnnotation(hierarchy, bsonDocumentAnnotation)) {
				result.add(annoEl);
			}
		}
		return result;
	}

	private Set<TypeElement> getAnnotationHierarchy(TypeElement annoTypeEl) {
		Set<TypeElement> result = new HashSet<>();
		getAnnotationHierarchy(annoTypeEl, result);
		return result;
	}

	private void getAnnotationHierarchy(TypeElement annoTypeEl, Set<TypeElement> result) {
		if (result.add(annoTypeEl)) {
			List<? extends AnnotationMirror> annos = annoTypeEl.getAnnotationMirrors();
			for (AnnotationMirror anno : annos) {
				DeclaredType annoDeclType = anno.getAnnotationType();
				Element annoEl = annoDeclType.asElement();
				if (annoEl.getKind() == ElementKind.ANNOTATION_TYPE) {
					getAnnotationHierarchy((TypeElement) annoEl, result);
				}
			}
		}
	}

	private boolean containsAnnotation(Set<TypeElement> elems, TypeElement annoEl) {
		for (TypeElement el : elems) {
			if (this.types.isSameType(annoEl.asType(), el.asType())) {
				return true;
			}
		}
		return false;
	}

}