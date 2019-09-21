package uk.dioxic.mgenerate.core.util;

import org.junit.jupiter.api.Test;

class FakerUtilTest {

    @Test
    void resolve_keyLookup() {
        FakerUtil.getValue("internet.free_email");
    }
}
