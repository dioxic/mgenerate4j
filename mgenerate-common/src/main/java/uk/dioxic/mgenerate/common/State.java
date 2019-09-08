package uk.dioxic.mgenerate.common;

import uk.dioxic.mgenerate.common.exception.DocumentNotMappedException;

public interface State {

    Object get(String coordinates) throws DocumentNotMappedException;

    Object get(Resolvable resolvable) throws DocumentNotMappedException;

    void clear();

}
