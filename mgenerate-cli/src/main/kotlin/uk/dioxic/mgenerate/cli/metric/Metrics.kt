package uk.dioxic.mgenerate.cli.metric

import com.mongodb.bulk.BulkWriteResult
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.InsertManyResult
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import uk.dioxic.mgenerate.cli.extension.isNotZeroOrEmpty
import uk.dioxic.mgenerate.cli.extension.percentile
import java.time.Duration.between
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime
import kotlin.time.toKotlinDuration

@ExperimentalTime
fun InsertManyResult.toMetric() = ResultMetric(
        batchCount = 1,
        insertedCount = insertedIds.size)

@ExperimentalTime
fun InsertOneResult.toMetric() = ResultMetric(
        batchCount = 1,
        insertedCount = 1)

@ExperimentalTime
fun DeleteResult.toMetric() = ResultMetric(
        batchCount = 1,
        deletedCount = deletedCount)

@ExperimentalTime
fun UpdateResult.toMetric() = ResultMetric(
        batchCount = 1,
        matchedCount = matchedCount,
        modifiedCount = modifiedCount,
        upsertCount = if (upsertedId === null) 0 else 1)

@ExperimentalTime
fun BulkWriteResult.toMetric() = ResultMetric(
        batchCount = 1,
        insertedCount = insertedCount,
        modifiedCount = modifiedCount.toLong(),
        deletedCount = deletedCount.toLong(),
        matchedCount = matchedCount.toLong(),
        upsertCount = upserts.size)

@ExperimentalTime
fun List<ResultMetric>.summarise(hideZeroAndEmpty: Boolean = true): Summary {
    val counts = this.reduce { acc, metric -> acc + metric }
    val startTime = this.minOf { it.executionTime }
    val endTime = this.maxOf { it.executionTime }
    val elapsedDuration = between(startTime, endTime).toKotlinDuration()

    fun latencyPercentile(percentile: Double) = this
            .map { it.duration }
            .percentile(percentile)

    fun rate(value: Number): Int {
        val rate = (value.toLong() / elapsedDuration.inSeconds)
        return if (!rate.isNaN()) rate.roundToInt() else 0
    }

    val summaries = listOf(
            "inserts/s" to rate(counts.insertedCount),
            "deletes/s" to rate(counts.deletedCount),
            "matched/s" to rate(counts.matchedCount),
            "modified/s" to rate(counts.modifiedCount),
            "upserts/s" to rate(counts.upsertCount),
            "operations/s" to rate(counts.operationCount),
            "bulk ops/s" to rate(counts.batchCount),
            "latency p50" to latencyPercentile(50.0),
            "latency p95" to latencyPercentile(95.0),
            "latency p99" to latencyPercentile(99.0)
    ).filter { !hideZeroAndEmpty || it.isNotZeroOrEmpty() }

    return Summary(summaries)

}