package uk.dioxic.mgenerate.cli.extension

import java.lang.StringBuilder
import java.time.LocalDateTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.toKotlinDuration

fun List<String>.printAlignWith(spacing: Int, other: List<String>): String {
    val sb = StringBuilder()

    require(this.size == other.size) { "List sizes need to be equal!"}

    forEachIndexed { idx, item ->
        sb.append(item.padEnd(spacing + (other[idx].length.coerceAtLeast(item.length)), ' '))
    }

    return sb.toString()
}

@ExperimentalTime
fun LocalDateTime.between(other: LocalDateTime): Duration = java.time.Duration.between(this, other).toKotlinDuration()