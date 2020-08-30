package uk.dioxic.mgenerate.cli.runner

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import uk.dioxic.mgenerate.cli.extension.*
import uk.dioxic.mgenerate.cli.metric.Summary
import java.util.concurrent.Callable
import kotlin.math.min
import kotlin.time.*

@FlowPreview
@ExperimentalTime
@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class Runner<T>(
        count: Long,
        parallelism: Int,
        val batchSize: Int,
        targetTps: Int = -1,
        monitorLoggingInterval: Duration = 1.seconds,
        hideZeroAndEmpty: Boolean = false,
        producer: () -> T,
        consumer: (List<T>) -> Any) : Callable<Duration> {

    private val productionDelay = 1000.milliseconds / targetTps

    val flow = flowOf(count, Duration.ZERO, producer)
//            .onEach { delay(productionDelay.toLongMilliseconds()) }
            .buffer(batchSize * 2)
            .chunked(batchSize)
            .onEach { delay((productionDelay * it.size).toLongMilliseconds()) }
            .mapParallel(parallelism) {
                measureTimedResultMetric(it.size) {
                    consumer(it)
                }
            }
            .monitor(
                    totalExecutions = count,
                    summaryFormat = Summary.SummaryFormat.SPACED,
                    loggingInterval = monitorLoggingInterval,
                    hideZeroAndEmpty = hideZeroAndEmpty
            )

    override fun call() = measureTime {
        runBlocking(Dispatchers.Default) {
            flow.collect { println(it) }
        }
    }

}