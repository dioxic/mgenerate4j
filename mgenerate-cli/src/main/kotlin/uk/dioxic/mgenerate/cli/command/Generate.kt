package uk.dioxic.mgenerate.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.output.CliktHelpFormatter
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.switch
import com.github.ajalt.clikt.parameters.types.defaultStdout
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.outputStream
import uk.dioxic.mgenerate.cli.extension.OutputType
import uk.dioxic.mgenerate.cli.extension.templateOf
import uk.dioxic.mgenerate.cli.extension.writeToOutputStream
import uk.dioxic.mgenerate.core.codec.TemplateCodec

class Generate : CliktCommand(help = "Generate data and output to a file or stdout") {
    init {
        context { helpFormatter = CliktHelpFormatter(showDefaultValues = true) }
    }

    private val number by option("-n", "--number", help = "Number of documents to generate").int().default(1)
    private val outputStream by option("-o", "--output", help = "Output file").outputStream().defaultStdout()
    private val outputType by option(help = "Output type").switch(
            "--pretty" to OutputType.PRETTY,
            "--array" to OutputType.ARRAY
    ).default(OutputType.NEWLINE)
    private val template by argument().convert { templateOf(it) }

    override fun run() {

        generateSequence { template }
                .take(number)
                .writeToOutputStream(outputStream, outputType, TemplateCodec())

    }
}