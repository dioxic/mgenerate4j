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
import com.mongodb.bulk.BulkWriteResult
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.BulkWriteOptions
import com.mongodb.client.model.InsertOneModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningReduce
import org.bson.codecs.configuration.CodecRegistries.fromCodecs
import uk.dioxic.mgenerate.cli.options.*
import uk.dioxic.mgenerate.core.Template
import uk.dioxic.mgenerate.core.codec.TemplateCodec
import java.time.LocalTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

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

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    override fun run() {
        val client = MongoClients.create(MongoClientSettings.builder()
                .applyAuthOptions(authOptions)
                .applyConnectionOptions(connOptions)
                .codecRegistry(fromCodecs(TemplateCodec()))
                .build()
        )

        val collection = client
                .getDatabase(namespaceOptions)
                .getCollection(namespaceOptions, Template::class.java)

        if (drop) collection.drop()

        val bulkWriteOptions = BulkWriteOptions().ordered(ordered)
        val batchRuns = number / batchSize

        runBlocking(Dispatchers.Default) {
            val start = System.currentTimeMillis()
            val templateChannel = produce(capacity = Channel.BUFFERED) {
                (0 until batchRuns).forEach { _ ->
                    send(Array(batchSize) { template })
                }
            }
            val metricChannel = Channel<Metric>(Channel.BUFFERED)
            val monitorChannel = Channel<Metric>(Channel.CONFLATED)

            launchMetricAccumulator(metricChannel, monitorChannel)
            launchMonitor(1.seconds, monitorChannel)

            launchWriters(collection,
                    templateChannel,
                    metricChannel,
                    bulkWriteOptions,
                    parallelism,
                    batchSize).joinAll()

            metricChannel.close()
            echo("Completed in ${(System.currentTimeMillis() - start) / 1000}s")
        }

    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    private fun CoroutineScope.launchMetricAccumulator(metricChannel: ReceiveChannel<Metric>,
                                                       monitorChannel: SendChannel<Metric>) = launch {
        metricChannel
                .receiveAsFlow()
                .runningReduce { accumulator, value -> accumulator.plus(value) }
                .collect { monitorChannel.send(it) }

        monitorChannel.close()
    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    private fun CoroutineScope.launchMonitor(refreshInterval: Duration, monitorChannel: ReceiveChannel<Metric>) = launch {
        var last = Metric(0, 0, 0, 0, 0, 0, 0)

        for (metric in monitorChannel) {
            delay(refreshInterval)
            val diff = metric.minus(last)
            echo(LocalTime.now())
            echo("total operations: ${metric.operationCount}, operations/s: ${diff.operationRate(refreshInterval)}, latency: ${diff.avgLatency()}")
            last = metric
        }
    }

    @ExperimentalTime
    private fun CoroutineScope.launchWriters(collection: MongoCollection<Template>,
                                             templateChannel: ReceiveChannel<Array<Template>>,
                                             metricChannel: SendChannel<Metric>,
                                             bulkWriteOptions: BulkWriteOptions,
                                             parallelism: Int,
                                             batchSize: Int): List<Job> = (0 until parallelism).map {
        launch {
            for (template in templateChannel) {
//                echo("writing from ${Thread.currentThread().name}")
                val metric = measureMetric(batchSize) {
//                    collection.insertMany(template.toMutableList(), InsertManyOptions().ordered(false))
                    collection.bulkWrite(template.map { InsertOneModel(it) }, bulkWriteOptions)
                }

                metricChannel.send(metric)
            }
        }
    }

    @ExperimentalTime
    private inline fun measureMetric(batchSize: Int, block: () -> BulkWriteResult): Metric {
        val start = System.currentTimeMillis()
        return Metric(block(), batchSize.toLong(), System.currentTimeMillis() - start)
    }

    @ExperimentalTime
    data class Metric(val batchCount: Long,
                      val operationCount: Long,
                      val insertedCount: Long,
                      val deletedCount: Long,
                      val matchedCount: Long,
                      val modifiedCount: Long,
                      val time: Long) {

        constructor(bulkWriteResult: BulkWriteResult, operationCount: Long, time: Long) : this(1,
                operationCount,
                bulkWriteResult.insertedCount.toLong(),
                bulkWriteResult.deletedCount.toLong(),
                bulkWriteResult.matchedCount.toLong(),
                bulkWriteResult.modifiedCount.toLong(),
                time)

        fun plus(metric: Metric): Metric {
            return Metric(batchCount.plus(metric.batchCount),
                    operationCount.plus(metric.operationCount),
                    insertedCount.plus(metric.insertedCount),
                    deletedCount.plus(metric.deletedCount),
                    matchedCount.plus(metric.matchedCount),
                    modifiedCount.plus(metric.modifiedCount),
                    time.plus(metric.time)
            )
        }

        fun minus(metric: Metric): Metric {
            return Metric(batchCount.minus(metric.batchCount),
                    operationCount.minus(metric.operationCount),
                    insertedCount.minus(metric.insertedCount),
                    deletedCount.minus(metric.deletedCount),
                    matchedCount.minus(metric.matchedCount),
                    modifiedCount.minus(metric.modifiedCount),
                    time.minus(metric.time)
            )
        }

        fun avgLatency(): Long = time / batchCount
        fun operationRate(duration: Duration): Long = (operationCount / duration.inSeconds).toLong()
    }

}
