package uk.dioxic.mgenerate.cli.runner

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.BulkWriteOptions
import com.mongodb.client.model.WriteModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import uk.dioxic.mgenerate.cli.extension.*
import uk.dioxic.mgenerate.cli.metric.Metric
import kotlin.contracts.ExperimentalContracts
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

class BulkRunner<T>(
        private val number: Long,
        private val parallelism: Int,
        private val batchSize: Int,
        private val collection: MongoCollection<T>,
        private val options: BulkWriteOptions,
        private val producer: () -> WriteModel<T>) : Runnable {

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @ExperimentalContracts
    override fun run() {
        runBlocking(Dispatchers.Default) {

            val batchChannel = produce(capacity = parallelism) {
                generateSequence { producer() }
                        .take(number.toInt())
                        .chunked(batchSize)
                        .forEach { send(it) }
            }

            val metricChannel = launchMonitor(loggingInterval =  1.seconds)

            val jobs = (1..parallelism).map {
                launchWorker(batchChannel, metricChannel) {
                    collection.bulkWrite(it, options)
                }
            }

            launch {
                jobs.joinAll()
                metricChannel.close()
            }

        }
    }

}