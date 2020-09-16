package uk.dioxic.mgenerate.cli.extension

import com.mongodb.bulk.BulkWriteResult
import com.mongodb.client.MongoIterable
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.InsertManyResult
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.*
import uk.dioxic.mgenerate.cli.metric.ResultMetric
import uk.dioxic.mgenerate.cli.metric.Summary
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimedValue
import kotlin.time.seconds

@ExperimentalTime
fun InsertManyResult.toMetric() = ResultMetric(
        insertedCount = insertedIds.size)

@ExperimentalTime
fun InsertOneResult.toMetric() = ResultMetric(
        insertedCount = 1)

@ExperimentalTime
fun DeleteResult.toMetric() = ResultMetric(
        deletedCount = deletedCount)

@ExperimentalTime
fun UpdateResult.toMetric() = ResultMetric(
        matchedCount = matchedCount,
        modifiedCount = modifiedCount,
        upsertCount = if (upsertedId === null) 0 else 1)

@ExperimentalTime
fun BulkWriteResult.toMetric() = ResultMetric(
        insertedCount = insertedCount,
        modifiedCount = modifiedCount.toLong(),
        deletedCount = deletedCount.toLong(),
        matchedCount = matchedCount.toLong(),
        upsertCount = upserts.size)

@ExperimentalTime
fun <T> MongoIterable<T>.toMetric(): ResultMetric {
    return ResultMetric(resultCount = count().toLong())
}

@ExperimentalTime
typealias TimedResultMetricBatch = TimedValue<List<ResultMetric>>

typealias SummaryFields = List<Pair<String, Any>>

@ExperimentalTime
internal fun summarizeDebug(metrics: TimedResultMetricBatch): SummaryFields {
    val workingDuration = metrics.value.fold(Duration.ZERO) { acc, metric -> acc + metric.duration }

    return listOf(
            "working duration" to workingDuration,
            "window duration" to metrics.duration,
    )
}

@ExperimentalTime
internal fun summarizeLoadFactor(metrics: TimedResultMetricBatch): SummaryFields {
    val workingDuration = metrics.value.fold(Duration.ZERO) { acc, metric -> acc + metric.duration }

    return listOf(
            "load factor" to (workingDuration percentOf metrics.duration)
    )
}

@ExperimentalTime
internal fun summarizeCounts(metrics: TimedResultMetricBatch): SummaryFields {
    val accumulator = metrics.value
            .fold(ResultMetric.ZERO) { acc, metric -> acc + metric }

    return listOf(
            "inserts" to accumulator.insertedCount,
            "deletes" to accumulator.deletedCount,
            "matched" to accumulator.matchedCount,
            "modified" to accumulator.modifiedCount,
            "upserts" to accumulator.upsertCount,
            "operations" to accumulator.operationCount,
            "bulk ops" to metrics.value.size,
    )
}

@ExperimentalTime
internal fun summarizeRates(metrics: TimedResultMetricBatch): SummaryFields {

    val accumulator = metrics.value
            .fold(ResultMetric.ZERO) { acc, metric -> acc + metric }

    fun rate(value: Number): Int {
        val rate = value.toLong() / metrics.duration.inSeconds
        return if (rate.isNaN() || rate.isInfinite()) 0 else rate.roundToInt()
    }

    return listOf(
            "inserts/s" to rate(accumulator.insertedCount),
            "deletes/s" to rate(accumulator.deletedCount),
            "matched/s" to rate(accumulator.matchedCount),
            "modified/s" to rate(accumulator.modifiedCount),
            "upserts/s" to rate(accumulator.upsertCount),
            "operations/s" to rate(accumulator.operationCount),
            "bulk ops/s" to rate(metrics.value.size),
    )
}

@ExperimentalTime
internal fun summarizeLatencies(metrics: TimedResultMetricBatch): SummaryFields {

    val listOrOne = if (metrics.value.isEmpty()) listOf(ResultMetric.ZERO) else metrics.value

    fun latencyPercentile(percentile: Double) = listOrOne
            .map { it.duration }
            .percentile(percentile)

    return listOf(
            "latency p50" to latencyPercentile(50.0),
            "latency p95" to latencyPercentile(95.0),
            "latency p99" to latencyPercentile(99.0),
    )
}

@ExperimentalTime
internal fun Flow<TimedResultMetricBatch>.summarize(vararg summarizers: (TimedResultMetricBatch) -> SummaryFields): Flow<Summary> = flow {
    collect {
        emit(Summary(summarizers
                .map { summarizer -> summarizer(it) }
                .flatten()))
    }
}

@ExperimentalTime
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
fun Flow<ResultMetric>.monitor(totalExecutions: Long,
                               loggingInterval: Duration = 1.seconds,
                               headerPrintInterval: Int = 10,
                               metricBufferSize: Int = 50000,
                               summaryFormat: Summary.SummaryFormat = Summary.SummaryFormat.SPACED,
                               hideZeroAndEmpty: Boolean = true): Flow<String> = flow {
    val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    var lineCounter: Long = 0
    var executionCounter: Long = 0

    onEach { executionCounter += it.operationCount }
            .chunkedTimeout(loggingInterval, metricBufferSize)
            .measureTimeValue()
            .summarize(
                    { listOf("time" to dtf.format(LocalDateTime.now())) },
                    ::summarizeRates,
//                    ::summarizeCounts,
                    ::summarizeLatencies,
                    ::summarizeLoadFactor,
//                    ::summarizeDebug,
                    { listOf("progress" to (executionCounter percentOf totalExecutions)) },
            )
            .map { if (hideZeroAndEmpty) it.filterNonEmpty() else it }
            .collect {
                if (lineCounter++ % headerPrintInterval == 0L) {
                    emit(it.headerString(summaryFormat))
                    emit(it.linebreakString(summaryFormat))
                }
                emit(it.valueString(summaryFormat))
            }
}