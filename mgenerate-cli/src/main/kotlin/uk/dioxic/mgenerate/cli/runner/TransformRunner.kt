package uk.dioxic.mgenerate.cli.runner

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import uk.dioxic.mgenerate.cli.extension.*
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

    @ObsoleteCoroutinesApi
    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun run() {
        val duration = measureTime {
            runBlocking(Dispatchers.Default) {

                flowOf(number, block = producer)
                        .buffer(Channel.BUFFERED)
                        .map { transformer(it) }
                        .chunked(batchSize)
                        .mapParallel(parallelism) {
                            measureTimedResultMetric(it.size) {
                                consumer(it)
                            }
                        }
                        .monitor(number, monitorLoggingInterval)
                        .collect { println(it) }
            }
        }
        println("Completed in $duration (${(number / duration.inSeconds).roundToInt()} docs/s)")
    }

}