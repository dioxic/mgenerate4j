package uk.dioxic.mgenerate.cli;

import org.bson.Document;
import org.bson.conversions.Bson;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.Mixin;
import uk.dioxic.mgenerate.cli.command.base.GenerateCommand;
import uk.dioxic.mgenerate.cli.command.template.GenerateTemplateCommand;
import uk.dioxic.mgenerate.cli.mixin.FormattingMixin;
import uk.dioxic.mgenerate.core.Template;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "mgenerate",
        header = "Mongo Generate CLI v0.0.6",
        description = "Mongo Generate CLI",
        subcommands = {
                HelpCommand.class
        }
)
public class Cli implements Callable<Integer> {

    @Mixin
    private FormattingMixin formattingMixin;

    private static CommandLine cl;
    private static final List<Object> generatorSubCommands = new ArrayList<>();

    public static void main(String[] args) {

        addGeneratorSubCommand(new GenerateTemplateCommand());

        CommandLine generate = new CommandLine(new GenerateCommand());
        CommandLine load = new CommandLine(new GenerateCommand());
        CommandLine query = new CommandLine(new GenerateCommand());

        generatorSubCommands.forEach(generate::addSubcommand);

        cl = new CommandLine(new Cli())
                .addSubcommand("generate", generate)
                .registerConverter(Template.class, s -> (s.startsWith("{")) ? Template.parse(s) : Template.from(s))
                .registerConverter(Bson.class, Document::parse)
                .setUsageHelpLongOptionsMaxWidth(45);

        System.exit(cl.execute(args));
    }

    public static void addGeneratorSubCommand(Object subCommand) {
        generatorSubCommands.add(subCommand);
    }

    @Override
    public Integer call() {
        cl.usage(System.out);
        return 0;
    }

}
