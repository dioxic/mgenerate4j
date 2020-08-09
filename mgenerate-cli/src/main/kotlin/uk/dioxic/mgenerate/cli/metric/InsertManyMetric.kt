package uk.dioxic.mgenerate.cli.metric

import com.mongodb.client.result.InsertManyResult
import uk.dioxic.mgenerate.cli.extension.between
import java.time.LocalDateTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
data class InsertManyMetric(
        override val timestamp: LocalDateTime,
        override val duration: Duration,
        override val operationCount: Long = 1L,
        override val elapsedDuration: Duration = duration,
        val insertedCount: Long) : Metric {

    constructor(result: InsertManyResult, duration: Duration) : this(
            timestamp = LocalDateTime.now(),
            duration = duration,
            insertedCount = result.insertedIds.size.toLong()
    )

    override operator fun plus(metric: Metric): InsertManyMetric {
        if (metric !is InsertManyMetric) {
            throw UnsupportedOperationException("cannot operate on different metric implementations")
        }

        return InsertManyMetric(
                timestamp = timestamp.coerceAtLeast(metric.timestamp),
                operationCount = operationCount + metric.operationCount,
                insertedCount = insertedCount + metric.insertedCount,
                duration = duration + metric.duration)
    }

    override operator fun minus(metric: Metric): InsertManyMetric {
        if (metric !is InsertManyMetric) {
            throw UnsupportedOperationException("cannot operate on different metric implementations")
        }
        return InsertManyMetric(
                timestamp = timestamp.coerceAtLeast(metric.timestamp),
                operationCount = operationCount - metric.operationCount,
                insertedCount = insertedCount - metric.insertedCount,
                elapsedDuration = metric.timestamp.between(timestamp),
                duration = duration - metric.duration)
    }

    override val summaryHeader = listOf("total", "inserts/s", "operations/s", "latency (ms)")
    override val summary
        get() = arrayOf(insertedCount,
                rate(insertedCount),
                rate(operationCount),
                latency()).map { it.toString() }

}