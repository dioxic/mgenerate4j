package uk.dioxic.mgenerate.cli.extension

import org.apache.commons.math3.stat.StatUtils
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@ExperimentalTime
fun isPositive(value: Any) = when (value) {
    is Long -> value > 0
    is Double -> value > 0
    is Float -> value > 0
    is Int -> value > 0
    is Duration -> value.isPositive()
    is String -> value.isNotEmpty()
    is List<*> -> value.isNotEmpty()
    else -> false
}

infix fun Int.percentOf(divisor: Int) = "${((this * 100) / divisor)}%"

infix fun Long.percentOf(divisor: Long) = "${((this * 100) / divisor)}%"

fun List<String>.printAlignWith(spacing: Int, other: List<String>): String {
    val sb = StringBuilder()

    require(this.size == other.size) { "List sizes need to be equal!" }

    forEachIndexed { idx, item ->
        sb.append(item.padEnd(spacing + (other[idx].length.coerceAtLeast(item.length)), ' '))
    }

    return sb.toString()
}

fun List<Pair<String, Any>>.firstAligned(spacing: Int): String =
        this.joinToString(separator = "") { alignWith(it.first, it.second.toString(), spacing) }

fun List<Pair<String, Any>>.secondAligned(spacing: Int): String =
        this.joinToString(separator = "") { alignWith(it.second.toString(), it.first, spacing) }

fun alignWith(outputString: String, alignmentString: String, spacing: Int) =
        outputString.padEnd(spacing + (alignmentString.length.coerceAtLeast(outputString.length)), ' ')

@ExperimentalTime
fun Iterable<Duration>.percentile(percentile: Double) = asSequence().percentile(percentile)

@ExperimentalTime
fun Sequence<Duration>.percentile(percentile: Double) = StatUtils.percentile(map { it.inMilliseconds }.toList().toDoubleArray(), percentile).toDuration(TimeUnit.MILLISECONDS)

@ExperimentalTime
fun Array<out Duration>.percentile(percentile: Double) = asSequence().percentile(percentile)