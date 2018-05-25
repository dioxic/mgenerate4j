package uk.dioxic.mgenerate.apt.poet;

import java.io.IOException;

public interface Poet {

    CharSequence getFullyQualifiedName();

    void generate(Appendable appendable) throws IOException;
}
