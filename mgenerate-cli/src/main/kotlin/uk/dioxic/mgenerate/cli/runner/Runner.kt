package uk.dioxic.mgenerate.cli.runner

import kotlinx.coroutines.*
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
class Runner<T>(
        count: Long,
        parallelism: Int,
        targetTps: Int = -1,
        monitorLoggingInterval: Duration = 1.seconds,
        hideZeroAndEmpty: Boolean = true,
        summaryFormat: SummaryFormat = SummaryFormat.SPACED,
        producer: () -> T,
        consumer: (T) -> Any) : Callable<Duration> {

    private val tpsDelay = 1000.milliseconds / targetTps

    private val flow = flowOf(count, producer)
            .onEach { delay(tpsDelay.toLongMilliseconds()) }
            .mapParallel(parallelism) {
                lockVariableState {
                    measureTimedResultMetric(1) {
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