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
import com.github.ajalt.clikt.parameters.types.path
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.bson.RawBsonDocument
import uk.dioxic.mgenerate.cli.extension.applyTemplateCodecRegistry
import uk.dioxic.mgenerate.cli.extension.templateOf
import uk.dioxic.mgenerate.cli.metric.ResultMetric
import uk.dioxic.mgenerate.cli.options.*
import uk.dioxic.mgenerate.cli.runner.Runner
import uk.dioxic.mgenerate.core.Template
import uk.dioxic.mgenerate.core.VariableCache
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime

class Find : CliktCommand(help = "Find documents in MongoDB") {
    init {
        context { helpFormatter = CliktHelpFormatter(showDefaultValues = true) }
    }

    private val authOptions by AuthOptions().cooccurring()
    private val connOptions by ConnectionOptions()
    private val namespaceOptions by NamespaceOptions()
    private val number by option("-n", "--number", help = "number of documents to load").long().default(1)
    private val tps by option(help = "target transactions per second").int().default(-1)
    private val parallelism by option(help = "parallelism of read operations").int().default(4)
    private val variables by option("-v", "--variables", help = "input variables file").path(mustExist = true, canBeDir = false)
    private val deserialize by option(help = "deserialize the results").flag()
    private val template by argument().convert { templateOf(it) }

    @FlowPreview
    @ExperimentalTime
    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    override fun run() {
        if (variables != null) {
            println("Loading variables file...")
            VariableCache.loadCache(variables)
            println("Variables loaded")
        }

        val client = MongoClients.create(MongoClientSettings.builder()
                .applyAuthOptions(authOptions)
                .applyConnectionOptions(connOptions)
                .applyTemplateCodecRegistry()
                .build()
        )

        val collection = client
                .getDatabase(namespaceOptions)
                .getCollection(namespaceOptions, Template::class.java)

        val duration = Runner(
                count = number,
                parallelism = parallelism,
                targetTps = tps,
                producer = { template },
                consumer = {
                    if (deserialize) {
                        collection.find(it, RawBsonDocument::class.java)
                    } else {
                        ResultMetric(resultCount = collection.countDocuments(it))
                    }
                }
        ).call()

        println("Completed in $duration (${(number / duration.inSeconds).roundToInt()} inserts/s)")
    }

}
