package uk.dioxic.mgenerate.cli.extension

import com.mongodb.bulk.BulkWriteResult
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
fun TimedValue<List<ResultMetric>>.summarise(hideZeroAndEmpty: Boolean = true): Summary =
        value.summarise(duration, hideZeroAndEmpty)

@ExperimentalTime
fun List<ResultMetric>.summarise(windowDuration: Duration, hideZeroAndEmpty: Boolean = true): Summary {
    val list = if (this.isNotEmpty()) this else listOf(ResultMetric.ZERO)

    fun latencyPercentile(percentile: Double) = list
            .map { it.duration }
            .percentile(percentile)

    val counts = list.reduce { acc, metric -> acc + metric }

    fun rate(value: Number): Int {
        val rate = value.toLong() / windowDuration.inSeconds
        return if (rate.isNaN() || rate.isInfinite()) -1 else rate.roundToInt()
    }

    val summaries = listOf(
            "inserts/s" to rate(counts.insertedCount),
            "deletes/s" to rate(counts.deletedCount),
            "matched/s" to rate(counts.matchedCount),
            "modified/s" to rate(counts.modifiedCount),
            "upserts/s" to rate(counts.upsertCount),
            "operations/s" to rate(counts.operationCount),
            "bulk ops/s" to rate(size),
            "latency p50" to latencyPercentile(50.0),
            "latency p95" to latencyPercentile(95.0),
            "latency p99" to latencyPercentile(99.0),
            "working duration" to counts.duration
    ).filter { !hideZeroAndEmpty || it.isNotZeroOrEmpty() }

    return Summary(summaries)

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
            .map { it.summarise(hideZeroAndEmpty) }
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