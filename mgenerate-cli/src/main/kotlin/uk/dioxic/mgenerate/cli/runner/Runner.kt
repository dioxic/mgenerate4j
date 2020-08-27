package uk.dioxic.mgenerate.cli.runner

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import uk.dioxic.mgenerate.cli.extension.*
import uk.dioxic.mgenerate.cli.metric.SummaryFormat
import java.util.concurrent.Callable
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.seconds

@FlowPreview
@ExperimentalTime
@ExperimentalCoroutinesApi
class Runner<T>(
        count: Long,
        parallelism: Int,
        batchSize: Int,
        monitorLoggingInterval: Duration = 1.seconds,
        hideZeroAndEmpty: Boolean = true,
        producer: () -> T,
        consumer: (List<T>) -> Any) : Callable<Duration> {

    val flow = flowOf(count, producer)
            .buffer(batchSize * 2)
            .chunked(batchSize)
            .mapParallel(parallelism) {
                measureTimedResultMetric(it.size) {
                    consumer(it)
                }
            }
            .monitor(
                    totalExecutions = count,
                    summaryFormat = SummaryFormat.SPACED,
                    loggingInterval = monitorLoggingInterval,
                    hideZeroAndEmpty = hideZeroAndEmpty
            )

    override fun call() = measureTime {
        runBlocking(Dispatchers.Default) {
            flow.collect { println(it) }
        }
    }

}