package uk.dioxic.mgenerate.cli.runner

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import uk.dioxic.mgenerate.cli.extension.chunked
import uk.dioxic.mgenerate.cli.extension.flowOf
import uk.dioxic.mgenerate.cli.extension.mapParallel
import uk.dioxic.mgenerate.cli.extension.monitor
import uk.dioxic.mgenerate.cli.metric.ResultMetric
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

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun run() {
        val duration = measureTime {
            runBlocking(Dispatchers.Default) {

                flowOf(number, producer)
                        .buffer(Channel.BUFFERED)
                        .map { transformer(it) }
                        .chunked(batchSize)
                        .mapParallel(parallelism) {
                            ResultMetric.create(it.size) { consumer(it) }
                        }
                        .monitor(number, monitorLoggingInterval)
                        .collect { println(it) }
            }
        }
        println("Completed in $duration (${(number / duration.inSeconds).roundToInt()} docs/s)")
    }

}