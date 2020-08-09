package uk.dioxic.mgenerate.cli.metric

import com.mongodb.bulk.BulkWriteResult
import com.mongodb.client.result.InsertManyResult
import java.time.LocalDateTime
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimedValue

@ExperimentalTime
interface Metric {
    val batchCount: Long
    val totalOperationCount: Long
    val duration: Duration
    val elapsedDuration: Duration
    val timestamp: LocalDateTime
    val summaryHeader: List<String>

    operator fun plus(metric: Metric): Metric
    operator fun minus(metric: Metric): Metric

    fun summary(totalCountExpected: Long): List<String>

    fun latency(): Long = duration.toLongMilliseconds() / batchCount
    fun rate(counter: Long): Int = (counter / elapsedDuration.inSeconds).roundToInt()
    fun progress(totalExpected: Long): String = ((totalOperationCount*100) / totalExpected).toString() + "%"

    companion object {
        fun create(result: TimedValue<out Any>, batchSize: Int): Metric {
            return when (result.value) {
                is BulkWriteResult -> BulkWriteMetric(result.value as BulkWriteResult, result.duration, batchSize)
                is InsertManyResult -> InsertManyMetric(result.value as InsertManyResult, result.duration, batchSize)
                else -> throw UnsupportedOperationException("${result.value.javaClass} not supported")
            }
        }
    }

}