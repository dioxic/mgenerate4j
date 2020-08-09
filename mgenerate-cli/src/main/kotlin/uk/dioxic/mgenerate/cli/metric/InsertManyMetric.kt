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
        override val batchCount: Long = 1L,
        override val elapsedDuration: Duration = duration,
        override val totalOperationCount: Long,
        val insertedCount: Long, ) : Metric {

    constructor(result: InsertManyResult, duration: Duration, batchSize: Int) : this(
            timestamp = LocalDateTime.now(),
            duration = duration,
            insertedCount = result.insertedIds.size.toLong(),
            totalOperationCount = batchSize.toLong()
    )

    override operator fun plus(metric: Metric): InsertManyMetric {
        if (metric !is InsertManyMetric) {
            throw UnsupportedOperationException("cannot operate on different metric implementations")
        }

        return InsertManyMetric(
                timestamp = timestamp.coerceAtLeast(metric.timestamp),
                batchCount = batchCount + metric.batchCount,
                insertedCount = insertedCount + metric.insertedCount,
                totalOperationCount = totalOperationCount + metric.totalOperationCount,
                duration = duration + metric.duration)
    }

    override operator fun minus(metric: Metric): InsertManyMetric {
        if (metric !is InsertManyMetric) {
            throw UnsupportedOperationException("cannot operate on different metric implementations")
        }
        return InsertManyMetric(
                timestamp = timestamp.coerceAtLeast(metric.timestamp),
                batchCount = batchCount - metric.batchCount,
                insertedCount = insertedCount - metric.insertedCount,
                totalOperationCount = totalOperationCount,
                elapsedDuration = metric.timestamp.between(timestamp),
                duration = duration - metric.duration)
    }

    override fun summary(totalCountExpected: Long): List<String> =
            arrayOf(rate(insertedCount),
                    rate(batchCount),
                    latency(),
                    progress(totalCountExpected)).map { it.toString() }

    override val summaryHeader = listOf("inserts/s", "operations/s", "latency (ms)", "progress")

}