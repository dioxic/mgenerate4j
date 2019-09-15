package uk.dioxic.mgenerate.core;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.util.FakerUtil;

class FakerTest {

    @Test
    void FakerUtil_resolve_keyLookup() {
        FakerUtil.getValue("internet.free_email");
    }
}
