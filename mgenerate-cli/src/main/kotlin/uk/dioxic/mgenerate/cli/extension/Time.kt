package uk.dioxic.mgenerate.cli.extension

import java.time.LocalDateTime
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.time.*


@ExperimentalTime
fun LocalDateTime.between(other: LocalDateTime): Duration = java.time.Duration.between(this, other).toKotlinDuration()