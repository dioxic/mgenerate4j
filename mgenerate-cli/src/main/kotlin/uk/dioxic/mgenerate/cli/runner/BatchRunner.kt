package uk.dioxic.mgenerate.cli.runner

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import uk.dioxic.mgenerate.cli.extension.*
import uk.dioxic.mgenerate.cli.metric.Summary.SummaryFormat
import java.util.concurrent.Callable
import kotlin.time.*

@FlowPreview
@ExperimentalTime
@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class BatchRunner<T>(
        count: Long,
        parallelism: Int,
        batchSize: Int,
        targetTps: Int = -1,
        monitorLoggingInterval: Duration = 1.seconds,
        hideZeroAndEmpty: Boolean = true,
        summaryFormat: SummaryFormat = SummaryFormat.SPACED,
        producer: () -> T,
        consumer: (List<T>) -> Any) : Callable<Duration> {

    private val tpsDelay = 1000.milliseconds / targetTps

    private val flow = flowOf(count, producer)
            .buffer(batchSize * 2)
            .chunked(batchSize)
            .onEach { delay((tpsDelay * it.size).toLongMilliseconds()) }
            .mapParallel(parallelism) {
                lockVariableState {
                    measureTimedResultMetric(it.size) {
                        consumer(it)
                    }
                }
            }
            .monitor(
                    totalExecutions = count,
                    summaryFormat = summaryFormat,
                    loggingInterval = monitorLoggingInterval,
                    hideZeroAndEmpty = hideZeroAndEmpty
            )

    override fun call() = measureTime {
        runBlocking(Dispatchers.Default) {
            flow.collect { println(it) }
        }
    }

}