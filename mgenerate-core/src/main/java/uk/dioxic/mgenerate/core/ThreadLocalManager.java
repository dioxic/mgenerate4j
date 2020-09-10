package uk.dioxic.mgenerate.core;

import uk.dioxic.mgenerate.core.DocumentCache.DocumentState;
import uk.dioxic.mgenerate.core.VariableCache.VariableState;

public class ThreadLocalManager {
    private static final ThreadLocal<ThreadLocalContext> localState = ThreadLocal.withInitial(ThreadLocalContext::new);

    public static ThreadLocalContext get() {
        return localState.get();
    }

    static class ThreadLocalContext {
        private DocumentState documentState;
        private VariableState variableState;
        private boolean variableStateLocked;

        public boolean isVariableStateLocked() {
            return variableStateLocked;
        }

        public void setVariableStateLocked(boolean variableStateLocked) {
            this.variableStateLocked = variableStateLocked;
        }

        public DocumentState getDocumentState() {
            return documentState;
        }

        public void setDocumentState(DocumentState documentState) {
            this.documentState = documentState;
        }

        public VariableState getVariableState() {
            return variableState;
        }

        public void setVariableState(VariableState variableState) {
            if (variableStateLocked && this.variableState != null) {
                throw new IllegalStateException("cannot set variable state while locked!");
            }
            this.variableState = variableState;
        }
    }

}
