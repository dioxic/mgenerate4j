package uk.dioxic.mgenerate.cli.runner

import kotlinx.coroutines.*
import uk.dioxic.mgenerate.cli.extension.*
import kotlin.contracts.ExperimentalContracts
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.seconds

@ExperimentalTime
class TransformRunner<T, M>(
        private val number: Long,
        private val parallelism: Int,
        private val batchSize: Int,
        private val monitorLoggingInterval: Duration = 1.seconds,
        private val producer: () -> T,
        private val transformer: (T) -> M,
        private val consumer: (List<M>) -> Any) : Runnable {

    @ExperimentalCoroutinesApi
    @ExperimentalContracts
    override fun run() {
        val duration = measureTime {
            runBlocking(Dispatchers.Default) {

                val producerChannel = launchProducer(number = number, producer = producer)
                val transformedChannel = launchTransformer(inputChannel = producerChannel, transformer = transformer)
                val batchChannel = launchBatcher(inputChannel = transformedChannel, batchSize = batchSize)
                val metricChannel = launchMonitor(loggingInterval = monitorLoggingInterval, totalExpected = number)

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