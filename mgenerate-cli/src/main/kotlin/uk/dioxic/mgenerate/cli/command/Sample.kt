package uk.dioxic.mgenerate.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.output.CliktHelpFormatter
import com.github.ajalt.clikt.parameters.groups.cooccurring
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.switch
import com.github.ajalt.clikt.parameters.types.defaultStdout
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.outputStream
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.model.Aggregates
import org.bson.codecs.DocumentCodec
import uk.dioxic.mgenerate.cli.extension.OutputType
import uk.dioxic.mgenerate.cli.extension.applyTemplateCodecRegistry
import uk.dioxic.mgenerate.cli.extension.writeToOutputStream
import uk.dioxic.mgenerate.cli.options.*

class Sample : CliktCommand(help = "Sample data in MongoDB") {
    init {
        context { helpFormatter = CliktHelpFormatter(showDefaultValues = true) }
    }

    private val authOptions by AuthOptions().cooccurring()
    private val connOptions by ConnectionOptions()
    private val namespaceOptions by NamespaceOptions()
    private val size by option(help = "sample size").int().default(10)
    private val outputStream by option("-o", "--output", help = "output file").outputStream().defaultStdout()
    private val outputType by option(help = "output type").switch(
            "--pretty" to OutputType.PRETTY,
            "--array" to OutputType.ARRAY
    ).default(OutputType.NEWLINE)

    override fun run() {
        val client = MongoClients.create(MongoClientSettings.builder()
                .applyAuthOptions(authOptions)
                .applyConnectionOptions(connOptions)
                .applyTemplateCodecRegistry()
                .build()
        )

        val collection = client
                .getDatabase(namespaceOptions)
                .getCollection(namespaceOptions)

        collection.aggregate(listOf(Aggregates.sample(size)))
                .writeToOutputStream(outputStream, outputType, DocumentCodec())

    }


}
