package uk.dioxic.mgenerate.cli.extension

import java.time.LocalDateTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.toKotlinDuration


@ExperimentalTime
fun LocalDateTime.between(other: LocalDateTime): Duration = java.time.Duration.between(this, other).toKotlinDuration()