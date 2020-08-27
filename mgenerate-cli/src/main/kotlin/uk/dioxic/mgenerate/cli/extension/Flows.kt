package uk.dioxic.mgenerate.cli.extension

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import uk.dioxic.mgenerate.cli.internal.RingBuffer
import uk.dioxic.mgenerate.cli.metric.ResultMetric
import uk.dioxic.mgenerate.cli.metric.Summary
import uk.dioxic.mgenerate.cli.metric.SummaryFormat
import uk.dioxic.mgenerate.cli.metric.summarise
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource
import kotlin.time.seconds

@Suppress("UNCHECKED_CAST")
@ExperimentalCoroutinesApi
fun <T> Flow<T>.runningDifference(operation: suspend (lastValue: T, value: T) -> T): Flow<T> = flow {
    var last: T? = null

    collect { value ->
        last = if (last === null) {
            value
        } else {
            emit(operation(last as T, value))
            value
        }
    }
}

/**
 * Returns a flow of lists each not exceeding the given [size].
 * The last list in the resulting flow may have less elements than the given [size].
 *
 * @param size the number of elements to take in each list, must be positive and can be greater than the number of elements in this flow.
 */
fun <T> Flow<T>.chunked(size: Int): Flow<List<T>> = chunked(size) { it.toList() }

/**
 * Chunks a flow of elements into flow of lists, each not exceeding the given [size]
 * and applies the given [transform] function to an each.
 *
 * Note that the list passed to the [transform] function is ephemeral and is valid only inside that function.
 * You should not store it or allow it to escape in some way, unless you made a snapshot of it.
 * The last list may have less elements than the given [size].
 *
 * This is slightly faster, than using flow.chunked(n).map { ... }
 *
 * @param size the number of elements to take in each list, must be positive and can be greater than the number of elements in this flow.
 */
fun <T, R> Flow<T>.chunked(size: Int, transform: suspend (List<T>) -> R): Flow<R> {
    require(size > 0) { "Size should be greater than 0, but was $size" }
    return windowed(size, size, true, transform)
}

/**
 * Returns a flow of lists each not exceeding the given [size]  or the timeout [timeout] interval
 * since the last element was received.
 *
 * The last list in the resulting flow may have less elements than the given [size].
 *
 * @param size the number of elements to take in each list, must be positive and can be greater than the number of elements in this flow.
 */
@ExperimentalTime
fun <T> Flow<T>.chunkedTimeout(timeout: Duration, size: Int): Flow<List<T>> = chunkedTimeout(timeout, size) { it.toList() }

/**
 * Chunks a flow of elements into flow of lists, each not exceeding the given [size] or the timeout [timeout] interval
 * since the last element was received and applies the given [transform] function to an each.
 *
 * Note that the list passed to the [transform] function is ephemeral and is valid only inside that function.
 * You should not store it or allow it to escape in some way, unless you made a snapshot of it.
 * The last list may have less elements than the given [size].
 *
 * This is slightly faster, than using flow.chunked(n).map { ... }
 *
 * @param size the number of elements to take in each list, must be positive and can be greater than the number of elements in this flow.
 */
@ExperimentalTime
fun <T, R> Flow<T>.chunkedTimeout(timeout: Duration, size: Int, transform: suspend (List<T>) -> R): Flow<R> {
    require(size > 0 && timeout.isPositive()) { "Timeout and size should be greater than 0, but was size: $size, timeout: $timeout" }
    return windowedTimeout(timeout, size, size, true, transform)
}

/**
 * Returns a flow of snapshots of the window of the given [size]
 * sliding along this flow with the given [step], where each
 * snapshot is a list.
 *
 * Several last lists may have less elements than the given [size].
 *
 * Both [size] and [step] must be positive and can be greater than the number of elements in this flow.
 * @param size the number of elements to take in each window
 * @param step the number of elements to move the window forward by on an each step
 * @param partialWindows controls whether or not to keep partial windows in the end if any.
 */
fun <T> Flow<T>.windowed(size: Int, step: Int, partialWindows: Boolean): Flow<List<T>> =
        windowed(size, step, partialWindows) { it.toList() }

/**
 * Returns a flow of results of applying the given [transform] function to
 * an each list representing a view over the window of the given [size]
 * sliding along this collection with the given [step].
 *
 * Note that the list passed to the [transform] function is ephemeral and is valid only inside that function.
 * You should not store it or allow it to escape in some way, unless you made a snapshot of it.
 * Several last lists may have less elements than the given [size].
 *
 * This is slightly faster, than using flow.windowed(...).map { ... }
 *
 * Both [size] and [step] must be positive and can be greater than the number of elements in this collection.
 * @param size the number of elements to take in each window
 * @param step the number of elements to move the window forward by on an each step.
 * @param partialWindows controls whether or not to keep partial windows in the end if any.
 */
fun <T, R> Flow<T>.windowed(size: Int, step: Int, partialWindows: Boolean, transform: suspend (List<T>) -> R): Flow<R> {
    require(size > 0 && step > 0) { "Size and step should be greater than 0, but was size: $size, step: $step" }

    return flow {
        val buffer = RingBuffer<T>(size)
        val toDrop = min(step, size)
        val toSkip = max(step - size, 0)
        var skipped = toSkip

        collect { value ->
            if (toSkip == skipped) buffer.add(value)
            else skipped++

            if (buffer.isFull()) {
                emit(transform(buffer))
                buffer.removeFirst(toDrop)
                skipped = 0
            }
        }

        while (partialWindows && buffer.isNotEmpty()) {
            emit(transform(buffer))
            buffer.removeFirst(min(toDrop, buffer.size))
        }
    }
}

/**
 * Returns a flow of snapshots of the window of the given [size] and timeout [timeout]
 * sliding along this flow with the given [step], where each
 * snapshot is a list.
 *
 * Note the timeout is only evaluated when a new message is received.
 *
 * Several last lists may have less elements than the given [size].
 *
 * Both [size] and [step] must be positive and can be greater than the number of elements in this flow.
 * @param size the number of elements to take in each window
 * @param step the number of elements to move the window forward by on an each step
 * @param partialWindows controls whether or not to keep partial windows in the end if any.
 */
@ExperimentalTime
fun <T> Flow<T>.windowedTimeout(timeout: Duration, size: Int, step: Int, partialWindows: Boolean): Flow<List<T>> =
        windowedTimeout(timeout, size, step, partialWindows) { it.toList() }

/**
 * Returns a flow of results of applying the given [transform] function to
 * an each list representing a view over the window of the given [size] and timeout [timeout]
 * sliding along this collection with the given [step].
 *
 * Note the timeout is only evaluated when a new message is received.
 *
 * Note that the list passed to the [transform] function is ephemeral and is valid only inside that function.
 * You should not store it or allow it to escape in some way, unless you made a snapshot of it.
 * Several last lists may have less elements than the given [size].
 *
 * This is slightly faster, than using flow.windowed(...).map { ... }
 *
 * [timeout], [size] and [step] must be positive and can be greater than the number of elements in this collection.
 * @param size the number of elements to take in each window
 * @param step the number of elements to move the window forward by on an each step.
 * @param partialWindows controls whether or not to keep partial windows in the end if any.
 */
@ExperimentalTime
fun <T, R> Flow<T>.windowedTimeout(timeout: Duration,
                                   size: Int,
                                   step: Int,
                                   partialWindows: Boolean,
                                   transform: suspend (List<T>) -> R): Flow<R> {
    require(timeout.isPositive() && size > 0 && step > 0) {
        "Duration, size and step should be greater than 0, but was duration: $timeout, size: $size, step: $step"
    }

    return flow {
        val buffer = RingBuffer<T>(size)
        val toDrop = min(step, size)
        val toSkip = max(step - size, 0)
        var skipped = toSkip
        var ts = TimeSource.Monotonic.markNow()

        collect { value ->
            if (toSkip == skipped) buffer.add(value)
            else skipped++

//            println("buffer size: ${buffer.size}")
//            if (buffer.isFull()) {
            if (buffer.isFull() || ts.elapsedNow() > timeout) {
                emit(transform(buffer))
                buffer.removeFirst(min(toDrop, buffer.size))
                skipped = 0
                ts = TimeSource.Monotonic.markNow()
            }
        }

        while (partialWindows && buffer.isNotEmpty()) {
            emit(transform(buffer))
            buffer.removeFirst(min(toDrop, buffer.size))
        }
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun <T, R> Flow<T>.mapParallel(
        parallelism: Int,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
        transform: suspend (T) -> R
): Flow<R> {
    return flatMapMerge(parallelism) { value ->
        flow { emit(transform(value)) }
    }.flowOn(dispatcher)
}

fun <T> flowOf(number: Long, block: () -> T): Flow<T> = flow {
    for (i in 1..number) {
        emit(block())
    }
}

@ExperimentalTime
fun Flow<ResultMetric>.monitor(totalExecutions: Long,
                               loggingInterval: Duration = 1.seconds,
                               headerPrintInterval: Int = 10,
                               metricBufferSize: Int = 50000,
                               summaryFormat: SummaryFormat = SummaryFormat.SPACED,
                               hideZeroAndEmpty: Boolean = true): Flow<String> = flow {
    val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    var lineCounter: Long = 0
    var executionCounter: Long = 0

    onEach { executionCounter += it.operationCount }
            .chunkedTimeout(loggingInterval, metricBufferSize) { it.summarise(hideZeroAndEmpty) }
            .map {
                it.add(
                        prefix = listOf("time" to dtf.format(LocalDateTime.now())),
                        suffix = listOf("progress" to (executionCounter percentOf totalExecutions))
                )
            }
            .collect {
                if (lineCounter++ % headerPrintInterval == 0L) {
                    emit(it.headerString(summaryFormat))
                    emit(it.linebreakString(summaryFormat))
                }
                emit(it.valueString(summaryFormat))
            }
}