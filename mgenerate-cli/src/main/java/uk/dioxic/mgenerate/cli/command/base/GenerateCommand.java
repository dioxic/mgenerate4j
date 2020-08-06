package uk.dioxic.mgenerate.cli.command.base;

import picocli.CommandLine.*;
import picocli.CommandLine.Model.CommandSpec;
import uk.dioxic.mgenerate.cli.mixin.FormattingMixin;

@Command(name = "generate",
        description = "generator command",
        subcommands = {
                HelpCommand.class
        })
public class GenerateCommand implements Runnable {

    @Spec
    CommandSpec spec;

    @Mixin
    FormattingMixin formattingMixin;

    @Override
    public void run() {
        throw new ParameterException(spec.commandLine(), "Specify a subcommand");
    }

}
