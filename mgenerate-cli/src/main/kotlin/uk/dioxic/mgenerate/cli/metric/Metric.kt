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
    val operationCount: Long
    val duration: Duration
    val elapsedDuration: Duration
    val timestamp: LocalDateTime
    val summary: List<String>
    val summaryHeader: List<String>

    operator fun plus(metric: Metric): Metric
    operator fun minus(metric: Metric): Metric

    fun latency(): Long = duration.toLongMilliseconds() / operationCount
    fun rate(counter: Long): Int = (counter / elapsedDuration.inSeconds).roundToInt()

    companion object {
        fun create(result: TimedValue<out Any>): Metric {
            return when (result.value) {
                is BulkWriteResult -> BulkWriteMetric(result.value as BulkWriteResult, result.duration)
                is InsertManyResult -> InsertManyMetric(result.value as InsertManyResult, result.duration)
                else -> throw UnsupportedOperationException("${result.value.javaClass} not supported")
            }
        }
    }

}