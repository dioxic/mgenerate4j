package uk.dioxic.mgenerate.cli.runner

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import uk.dioxic.mgenerate.cli.extension.*
import uk.dioxic.mgenerate.cli.metric.ResultMetric
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.seconds

@ExperimentalTime
class Runner<T>(
        private val count: Long,
        private val parallelism: Int,
        private val batchSize: Int,
        private val monitorLoggingInterval: Duration = 1.seconds,
        private val producer: () -> T,
        private val consumer: (List<T>) -> Any) : Runnable {

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun run() {
        val duration = measureTime {
            runBlocking(Dispatchers.Default) {

                flowOf(count, producer)
                        .buffer(batchSize * 2)
                        .chunked(batchSize)
                        .mapParallel(parallelism) {
                            ResultMetric.create(it.size) { consumer(it) }
                        }
                        .monitor(count, monitorLoggingInterval)
                        .collect { println(it) }
            }
        }
        println("Completed in $duration (${(count / duration.inSeconds).roundToInt()} docs/s)")
    }

}