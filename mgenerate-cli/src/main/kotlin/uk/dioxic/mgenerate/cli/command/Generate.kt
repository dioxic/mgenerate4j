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
import org.bson.json.JsonWriterSettings
import uk.dioxic.mgenerate.cli.command.Generate.OutputType.*
import uk.dioxic.mgenerate.core.Template

class Generate : CliktCommand(help = "Generate data and output to a file or stdout") {
    init {
        context { helpFormatter = CliktHelpFormatter(showDefaultValues = true) }
    }

    private enum class OutputType {
        PRETTY, ARRAY, NEWLINE
    }

    private val number by option("-n", "--number", help = "Number of documents to generate").int().default(1)
    private val outputStream by option("-o", "--output", help = "Output file").outputStream().defaultStdout()
    private val outputType by option(help = "Output type").switch(
            "--pretty" to PRETTY,
            "--array" to ARRAY
    ).default(NEWLINE)
    private val template by argument().convert { if (it.startsWith("{")) Template.parse(it) else Template.from(it) }

    override fun run() {
        val jws = JsonWriterSettings.builder().indent(outputType == PRETTY).build()

        outputStream.bufferedWriter().use {
            if (outputType == ARRAY) it.append('[')

            for (x in 0 until number - 1) {
                template.writeJson(jws, it)

                if (outputType == ARRAY) it.append(",") else it.newLine()
            }

            template.writeJson(jws, it)

            if (outputType == ARRAY) it.append(']')
        }

        if (outputType == ARRAY) echo("output written to file.")
    }
}