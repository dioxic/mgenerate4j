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
import com.mongodb.client.model.InsertManyOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.bson.codecs.configuration.CodecRegistries.fromCodecs
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import uk.dioxic.mgenerate.cli.options.*
import uk.dioxic.mgenerate.cli.runner.Runner
import uk.dioxic.mgenerate.core.Template
import uk.dioxic.mgenerate.core.codec.MgenDocumentCodec
import uk.dioxic.mgenerate.core.codec.TemplateCodec
import kotlin.contracts.ExperimentalContracts
import kotlin.time.ExperimentalTime

class Load : CliktCommand(help = "Load data directly into MongoDB") {
    init {
        context { helpFormatter = CliktHelpFormatter(showDefaultValues = true) }
    }

    private val authOptions by AuthOptions().cooccurring()
    private val connOptions by ConnectionOptions()
    private val namespaceOptions by NamespaceOptions()
    private val number by option("-n", "--number", help = "Number of documents to generate").long().default(1)
    private val batchSize by option("-b", "--batchsize", help = "Number of operations to batch together").int().default(100)
    private val parallelism by option(help = "parallelism of write operations").int().default(4)
    private val drop by option(help = "drop collection before load").flag()
    private val ordered by option(help = "enable ordered writes").flag()
    private val template by argument().convert { if (it.startsWith("{")) Template.parse(it) else Template.from(it) }

    @FlowPreview
    @ExperimentalContracts
    @ExperimentalTime
    @ExperimentalCoroutinesApi
    override fun run() {
        val client = MongoClients.create(MongoClientSettings.builder()
                .applyAuthOptions(authOptions)
                .applyConnectionOptions(connOptions)
                .codecRegistry(fromRegistries(fromCodecs(TemplateCodec()), MgenDocumentCodec.getCodecRegistry()))
                .build()
        )

        val collection = client
                .getDatabase(namespaceOptions)
                .getCollection(namespaceOptions, Template::class.java)

        if (drop) collection.drop()

//        val bulkWriteOptions = BulkWriteOptions().ordered(ordered)
        val insertManyOptions = InsertManyOptions().ordered(ordered)

        Runner(
                number = number,
                parallelism = parallelism,
                batchSize = batchSize,
                producer = { template },
                consumer = { collection.insertMany(it, insertManyOptions) }
        ).run()

//
//        Runner(
//                number = number,
//                parallelism = parallelism,
//                batchSize = batchSize,
//                producer = { InsertOneModel(template) },
//                consumer = { collection.bulkWrite(it, bulkWriteOptions) }
//        ).run()

//        TransformRunner(
//                number = number,
//                parallelism = parallelism,
//                batchSize = batchSize,
//                producer = { template },
//                transformer = {
//                    val doc = it.toBsonDocument()
//
//                    UpdateOneModel<BsonDocument>(
//                            Filters.eq("_id", doc["_id"]),
//                            Updates.set("name", doc["name"]),
//                            UpdateOptions().upsert(true))
//                },
//                consumer = {
//                    collection.bulkWrite(it)
//                }
//        ).run()


    }

}
