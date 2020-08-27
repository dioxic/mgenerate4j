package uk.dioxic.mgenerate.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.output.CliktHelpFormatter
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.groups.cooccurring
import com.github.ajalt.clikt.parameters.groups.provideDelegate
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.long
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.model.BulkWriteOptions
import com.mongodb.client.model.DeleteOneModel
import com.mongodb.client.model.DeleteOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import uk.dioxic.mgenerate.cli.extension.applyTemplateCodecRegistry
import uk.dioxic.mgenerate.cli.extension.templateOf
import uk.dioxic.mgenerate.cli.options.*
import uk.dioxic.mgenerate.cli.runner.Runner
import uk.dioxic.mgenerate.core.Template
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime

class Delete : CliktCommand(help = "Delete data in MongoDB") {
    init {
        context { helpFormatter = CliktHelpFormatter(showDefaultValues = true) }
    }

    private val authOptions by AuthOptions().cooccurring()
    private val connOptions by ConnectionOptions()
    private val namespaceOptions by NamespaceOptions()
    private val number by option("-n", "--number", help = "number of operations to execute").long().default(1)
    private val batchSize by option("-b", "--batchsize", help = "number of operations to batch together").int().default(100)
    private val parallelism by option(help = "parallelism of write operations").int().default(4)
    private val drop by option(help = "drop collection before load").flag()
    private val ordered by option(help = "enable ordered writes").flag()
    private val filterTemplate by argument("FILTER").convert { templateOf(it) }

    @FlowPreview
    @ExperimentalTime
    @ExperimentalCoroutinesApi
    override fun run() {
        val client = MongoClients.create(MongoClientSettings.builder()
                .applyAuthOptions(authOptions)
                .applyConnectionOptions(connOptions)
                .applyTemplateCodecRegistry()
                .build()
        )

        val collection = client
                .getDatabase(namespaceOptions)
                .getCollection(namespaceOptions, Template::class.java)

        if (drop) collection.drop()

        val bulkWriteOptions = BulkWriteOptions().ordered(ordered)
        val deleteOptions = DeleteOptions()

        val duration = Runner(
                count = number,
                parallelism = parallelism,
                batchSize = batchSize,
                producer = {
                    DeleteOneModel<Template>(
                            filterTemplate,
                            deleteOptions
                    )
                },
                consumer = { collection.bulkWrite(it, bulkWriteOptions) }
        ).call()

        println("Completed in $duration (${(number / duration.inSeconds).roundToInt()} deletes/s)")

    }

}
