package uk.dioxic.mgenerate.cli.mixin;

import picocli.CommandLine.Command;

@Command(//mixinStandardHelpOptions = true,
        requiredOptionMarker = '*',
        synopsisHeading = "%nUsage:%n  ",
        optionListHeading = "%nOptions:%n",
        parameterListHeading = "%nParameters:%n",
        descriptionHeading = "%nDescription:%n  ",
        commandListHeading = "%nCommands:%n",
        separator = " ",
        version = "v0.0.1",
        abbreviateSynopsis = true,
        usageHelpAutoWidth = true)
public class FormattingMixin {
}
