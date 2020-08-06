package uk.dioxic.mgenerate.cli.mixin

import org.bson.json.JsonWriterSettings
import picocli.CommandLine.Option
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class GenerateMixin {
    @Option(names = ["-o", "--out"],
            description = ["output file"],
            paramLabel = "arg")
    var file: Path? = null

    @Option(names = ["-n", "--number"],
            description = ["number of documents to generate (default: \${DEFAULT-VALUE})"],
            defaultValue = "5",
            paramLabel = "arg")
    var number: Int = 5

    @Option(names = ["-p", "--pretty"],
            description = ["pretty print output"])
    var pretty = false

    @Option(names = ["-a", "--array"],
            description = ["output as json array"])
    var array = false

    @Throws(IOException::class)
    fun generate(streamWriter: StreamWriter) {
        val jws = JsonWriterSettings.builder()
                .indent(pretty)
                .build()

        if (file != null) {
            Files.newBufferedWriter(file, StandardOpenOption.CREATE).use { writer -> writeJson(jws, writer, streamWriter) }
        } else {
            BufferedWriter(OutputStreamWriter(System.out)).use { writer -> writeJson(jws, writer, streamWriter) }
        }
    }

    @Throws(IOException::class)
    private fun writeJson(jws: JsonWriterSettings, writer: Writer, streamWriter: StreamWriter) {
        val delimiter = if (array) "," else "\n"
        if (array) {
            writer.write("[")
        }
        for (i in 0 until number - 1) {
            streamWriter.write(jws, writer)
            writer.append(delimiter)
        }
        streamWriter.write(jws, writer)
        if (array) {
            writer.write("]")
        }
    }

    interface StreamWriter {
        fun write(jws: JsonWriterSettings?, writer: Writer?)
    }
}