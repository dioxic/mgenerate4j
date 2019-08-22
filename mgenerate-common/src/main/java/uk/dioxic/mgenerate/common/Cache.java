package uk.dioxic.mgenerate.common;

import uk.dioxic.mgenerate.common.exception.DocumentNotMappedException;

public interface Cache {

    Object get(String coordinates) throws DocumentNotMappedException;

    void clear();

}
