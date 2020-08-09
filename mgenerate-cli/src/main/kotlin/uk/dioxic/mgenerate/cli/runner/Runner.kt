package uk.dioxic.mgenerate.cli.runner

import kotlinx.coroutines.*
import uk.dioxic.mgenerate.cli.extension.launchBatchProducer
import uk.dioxic.mgenerate.cli.extension.launchMonitor
import uk.dioxic.mgenerate.cli.extension.launchWorkers
import kotlin.contracts.ExperimentalContracts
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.seconds

@ExperimentalTime
class Runner<T>(
        private val number: Long,
        private val parallelism: Int,
        private val batchSize: Int,
        private val monitorLoggingInterval: Duration = 1.seconds,
        private val producer: () -> T,
        private val consumer: (List<T>) -> Any) : Runnable {

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @ExperimentalContracts
    override fun run() {
        val duration = measureTime {
            runBlocking(Dispatchers.Default) {

                val batchChannel = launchBatchProducer(capacity = parallelism,
                        batchSize = batchSize,
                        number = number,
                        producer = producer)

                val metricChannel = launchMonitor(
                        loggingInterval = monitorLoggingInterval,
                        totalExpected = number
                )

                val jobs = launchWorkers(
                        batchSize = batchSize,
                        parallelism = parallelism,
                        inputChannel = batchChannel,
                        metricChannel = metricChannel,
                        consumer = consumer)

                launch {
                    jobs.joinAll()
                    metricChannel.close()
                }
            }
        }
        println("Completed in $duration (${(number / duration.inSeconds).roundToInt()} docs/s)")
    }

}