package uk.dioxic.mgenerate.cli.metric

import com.mongodb.bulk.BulkWriteResult
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.InsertManyResult
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import java.time.LocalDateTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
data class ResultMetric(
        val duration: Duration = Duration.ZERO,
        val operationCount: Int = 0,
        val batchCount: Int = 0,
        val insertedCount: Int = 0,
        val upsertCount: Int = 0,
        val deletedCount: Long = 0,
        val matchedCount: Long = 0,
        val modifiedCount: Long = 0,
        val executionTime: LocalDateTime = LocalDateTime.now()) {

    operator fun plus(other: ResultMetric): ResultMetric {
        return ResultMetric(
                duration = duration + other.duration,
                batchCount = batchCount + other.batchCount,
                operationCount = operationCount + other.operationCount,
                insertedCount = insertedCount + other.insertedCount,
                upsertCount = upsertCount + other.upsertCount,
                deletedCount = deletedCount + other.deletedCount,
                matchedCount = matchedCount + other.matchedCount,
                modifiedCount = modifiedCount + other.modifiedCount)
    }

    companion object {
        fun <T> create(value: T) =
                when (value) {
                    is ResultMetric -> value
                    is BulkWriteResult -> value.toMetric()
                    is InsertOneResult -> value.toMetric()
                    is InsertManyResult -> value.toMetric()
                    is DeleteResult -> value.toMetric()
                    is UpdateResult -> value.toMetric()
                    else -> {
                        throw UnsupportedOperationException("Cannot create a ResultMetric from ${value!!::class.simpleName} class type")
                    }
                }
    }
}