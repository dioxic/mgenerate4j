package uk.dioxic.mgenerate.core.util;

import uk.dioxic.mgenerate.core.Template;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtil {

    public static List<Template> getTemplates(Path path) throws IOException {
        if (Files.exists(path)) {
            if (Files.isDirectory(path)) {
                return Files.walk(path)
                        .map(templateFile -> {
                            try {
                                return Template.from(templateFile);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toList());
            }
            return Collections.singletonList(Template.from(path));
        }
        return Collections.emptyList();
    }

}
