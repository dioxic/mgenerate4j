package uk.dioxic.mgenerate.cli.command.template;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.bson.json.JsonWriterSettings;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Parameters;
import uk.dioxic.mgenerate.cli.mixin.FormattingMixin;
import uk.dioxic.mgenerate.cli.mixin.GenerateMixin;
import uk.dioxic.mgenerate.core.Template;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.Callable;

@Slf4j
@Setter
@Command(
        name="template",
        description = "generates documents from an mgenerate template"
)
public class GenerateTemplateCommand implements Callable<Integer>, GenerateMixin.StreamWriter {

    @Mixin
    private FormattingMixin formattingMixin;

    @Mixin
    private GenerateMixin mixin;

    @Parameters(paramLabel = "TEMPLATE",
            description = "mgenerate template file or template string")
    private Template template;

    @Override
    public Integer call() throws IOException {
        mixin.generate(this);

        return 0;
    }

    @Override
    public void write(JsonWriterSettings jws, Writer writer) {
        template.writeJson(jws, writer);
    }
}
