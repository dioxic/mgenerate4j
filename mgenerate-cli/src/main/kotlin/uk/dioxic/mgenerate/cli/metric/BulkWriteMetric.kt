package uk.dioxic.mgenerate.cli.metric

import com.mongodb.bulk.BulkWriteResult
import uk.dioxic.mgenerate.cli.extension.between
import java.time.LocalDateTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
data class BulkWriteMetric(
        override val duration: Duration,
        override val batchCount: Long = 1L,
        override val timestamp: LocalDateTime,
        override val elapsedDuration: Duration = duration,
        override val totalOperationCount: Long,
        val insertedCount: Long,
        val upsertCount: Long,
        val deletedCount: Long,
        val matchedCount: Long,
        val modifiedCount: Long) : Metric {

    constructor(result: BulkWriteResult, duration: Duration, batchSize: Int) : this(
            timestamp = LocalDateTime.now(),
            duration = duration,
            upsertCount = result.upserts.size.toLong(),
            insertedCount = result.insertedCount.toLong(),
            deletedCount = result.deletedCount.toLong(),
            matchedCount = result.matchedCount.toLong(),
            modifiedCount = result.modifiedCount.toLong(),
            totalOperationCount = batchSize.toLong()
    )

    override operator fun plus(metric: Metric): BulkWriteMetric {
        if (metric !is BulkWriteMetric) {
            throw UnsupportedOperationException("cannot operate on different metric implementations")
        }

        return BulkWriteMetric(
                timestamp = timestamp.coerceAtLeast(metric.timestamp),
                duration = duration + metric.duration,
                batchCount = batchCount + metric.batchCount,
                upsertCount = upsertCount + metric.upsertCount,
                insertedCount = insertedCount + metric.insertedCount,
                deletedCount = deletedCount + metric.deletedCount,
                matchedCount = matchedCount + metric.matchedCount,
                modifiedCount = modifiedCount + metric.modifiedCount,
                totalOperationCount = totalOperationCount + metric.totalOperationCount
        )
    }

    override operator fun minus(metric: Metric): BulkWriteMetric {
        if (metric !is BulkWriteMetric) {
            throw UnsupportedOperationException("cannot operate on different metric implementations")
        }
        return BulkWriteMetric(
                timestamp = timestamp.coerceAtLeast(metric.timestamp),
                duration = duration - metric.duration,
                elapsedDuration = metric.timestamp.between(timestamp),
                batchCount = batchCount - metric.batchCount,
                upsertCount = upsertCount - metric.upsertCount,
                insertedCount = insertedCount - metric.insertedCount,
                deletedCount = deletedCount - metric.deletedCount,
                matchedCount = matchedCount - metric.matchedCount,
                modifiedCount = modifiedCount - metric.modifiedCount,
                totalOperationCount = totalOperationCount - metric.totalOperationCount
        )
    }

    override val summaryHeader = listOf("inserts/s", "deletes/s", "matched/s", "modified/s", "upserts/s", "batches/s", "latency (ms)", "progress")

    override fun summary(totalCountExpected: Long) = arrayOf(rate(insertedCount),
            rate(deletedCount),
            rate(matchedCount),
            rate(modifiedCount),
            rate(upsertCount),
            rate(batchCount),
            latency(),
            progress(totalCountExpected)).map { it.toString() }

}