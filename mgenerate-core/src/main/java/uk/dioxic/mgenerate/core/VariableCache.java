package uk.dioxic.mgenerate.core;

import org.bson.Document;
import uk.dioxic.mgenerate.core.ThreadLocalManager.ThreadLocalContext;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class VariableCache {

    private static VariableCache instance;

    public static VariableCache getInstance() {
        if (instance == null) {
            throw new IllegalStateException("variable cache has not been loaded yet!");
        }

        return instance;
    }

    public static boolean canHandle(String key) {
        return key.startsWith("$$");
    }

    public static void loadCache(String filename) {
        instance = new VariableCache(filename);
    }

    public static void loadCache(Path path) {
        instance = new VariableCache(path);
    }

    public static void lock() {
        ThreadLocalManager.get().setVariableStateLocked(true);
    }

    public static void unlock() {
        ThreadLocalManager.get().setVariableStateLocked(false);
    }

    public static void next() {
        ThreadLocalContext context = ThreadLocalManager.get();
        if (instance != null && !context.isVariableStateLocked()) {
            context.setVariableState(instance.getRandomItem());
        }
    }

    public static VariableState getState() {
        ThreadLocalContext context = ThreadLocalManager.get();
        VariableState state = context.getVariableState();
        if (state == null) {
            state = getInstance().getRandomItem();
            context.setVariableState(state);
        }

        return state;
    }

    public static Object get(String coordinate) {
        return getState().get(coordinate);
    }

    private final List<VariableState> cache;

    private VariableCache(Path path) {
        try {
            cache = Files.readAllLines(path, StandardCharsets.UTF_8)
                    .stream()
                    .map(Document::parse)
                    .map(VariableState::new)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private VariableCache(String filename) {
        this(Paths.get(filename));
    }

    public VariableState getRandomItem() {
        return cache.get(FakerUtil.numberBetween(0, cache.size()));
    }

    static class VariableState {
        private final Document document;

        public VariableState(Document document) {
            this.document = document;
        }

        public Object get(String coordinates) {
            String[] splitCo = coordinates.split("\\.");

            Object element = document;

            for (String s : splitCo) {
                if (element == null) {
                    return null;
                }
                if (element instanceof List) {
                    List<?> l = (List<?>) element;
                    element = l.get(FakerUtil.numberBetween(0, l.size()));
                }
                if (element instanceof Document) {
                    Document doc = (Document) element;
                    element = doc.get(s);
                }
            }

            return element;
        }
    }
}
