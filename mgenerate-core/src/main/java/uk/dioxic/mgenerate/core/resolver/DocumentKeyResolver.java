package uk.dioxic.mgenerate.core.resolver;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.core.DocumentStateCache;
import uk.dioxic.mgenerate.core.codec.TemplateCodec;

public class DocumentKeyResolver implements Resolvable {
    private final String documentKey;

    DocumentKeyResolver(String documentKey) {
        this.documentKey = documentKey;
        TemplateCodec.keyResolverFound();
    }

    @Override
    public Object resolve() {
        return DocumentStateCache.get(documentKey);
    }

}