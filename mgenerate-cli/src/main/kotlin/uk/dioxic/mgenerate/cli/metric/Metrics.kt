package uk.dioxic.mgenerate.cli.metric

import com.mongodb.bulk.BulkWriteResult
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.InsertManyResult
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import uk.dioxic.mgenerate.cli.extension.firstAligned
import uk.dioxic.mgenerate.cli.extension.isPositive
import uk.dioxic.mgenerate.cli.extension.percentile
import uk.dioxic.mgenerate.cli.extension.secondAligned
import java.time.Duration.between
import java.time.LocalDateTime
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimedValue
import kotlin.time.toKotlinDuration

@ExperimentalTime
data class ResultMetric(
        val duration: Duration,
        val batchSize: Int,
        val operationCount: Int = 1,
        val insertedCount: Int = 0,
        val upsertCount: Int = 0,
        val deletedCount: Long = 0,
        val matchedCount: Long = 0,
        val modifiedCount: Long = 0,
        val executionTime: LocalDateTime = LocalDateTime.now()) {

    operator fun plus(other: ResultMetric): ResultMetric {
        return ResultMetric(
                duration = duration + other.duration,
                batchSize = batchSize + other.batchSize,
                operationCount = operationCount + other.operationCount,
                insertedCount = insertedCount + other.insertedCount,
                upsertCount = upsertCount + other.upsertCount,
                deletedCount = deletedCount + other.deletedCount,
                matchedCount = matchedCount + other.matchedCount,
                modifiedCount = modifiedCount + other.modifiedCount)
    }

    companion object {
        fun <T> create(timedValue: TimedValue<T>, batchSize: Int) =
                when (val value = timedValue.value) {
                    is BulkWriteResult -> value.toMetric(timedValue.duration, batchSize)
                    is InsertOneResult -> value.toMetric(timedValue.duration, batchSize)
                    is InsertManyResult -> value.toMetric(timedValue.duration, batchSize)
                    is DeleteResult -> value.toMetric(timedValue.duration, batchSize)
                    is UpdateResult -> value.toMetric(timedValue.duration, batchSize)
                    else -> {
                        throw UnsupportedOperationException("Cannot create a ResultMetric from ${value!!::class.simpleName} class type")
                    }
                }
    }
}

@ExperimentalTime
data class Summary(val p50Latency: Duration,
                   val p95Latency: Duration,
                   val p99Latency: Duration,
                   val elapsedDuration: Duration,
                   val operationRate: Int,
                   val insertedRate: Int,
                   val upsertRate: Int,
                   val deletedRate: Int,
                   val matchedRate: Int,
                   val modifiedRate: Int) {

    val summaryFields: List<Pair<String, Any>> = listOf(
            "inserts/s" to insertedRate,
            "deletes/s" to deletedRate,
            "matched/s" to matchedRate,
            "modified/s" to modifiedRate,
            "upserts/s" to upsertRate,
            "batches/s" to operationRate,
            "p50 latency" to p50Latency,
            "p95 latency" to p95Latency,
            "p99 latency" to p99Latency
    )
}

@ExperimentalTime
fun InsertManyResult.toMetric(duration: Duration, batchSize: Int) = ResultMetric(
        duration = duration,
        batchSize = batchSize,
        insertedCount = insertedIds.size)

@ExperimentalTime
fun InsertOneResult.toMetric(duration: Duration, batchSize: Int) = ResultMetric(
        duration = duration,
        batchSize = batchSize,
        insertedCount = 1)

@ExperimentalTime
fun DeleteResult.toMetric(duration: Duration, batchSize: Int) = ResultMetric(
        duration = duration,
        batchSize = batchSize,
        deletedCount = deletedCount)

@ExperimentalTime
fun UpdateResult.toMetric(duration: Duration, batchSize: Int) = ResultMetric(
        duration = duration,
        batchSize = batchSize,
        matchedCount = matchedCount,
        modifiedCount = modifiedCount,
        upsertCount = if (upsertedId === null) 0 else 1)

@ExperimentalTime
fun BulkWriteResult.toMetric(duration: Duration, batchSize: Int) = ResultMetric(
        duration = duration,
        batchSize = batchSize,
        insertedCount = insertedCount,
        modifiedCount = modifiedCount.toLong(),
        deletedCount = deletedCount.toLong(),
        matchedCount = matchedCount.toLong(),
        upsertCount = upserts.size)

@ExperimentalTime
fun List<ResultMetric>.summarise(): Summary {
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

    return Summary(
            p50Latency = latencyPercentile(50.0),
            p95Latency = latencyPercentile(95.0),
            p99Latency = latencyPercentile(99.0),
            elapsedDuration = elapsedDuration,
            operationRate = rate(counts.operationCount),
            insertedRate = rate(counts.insertedCount),
            upsertRate = rate(counts.upsertCount),
            deletedRate = rate(counts.deletedCount),
            matchedRate = rate(counts.matchedCount),
            modifiedRate = rate(counts.modifiedCount)
    )

}