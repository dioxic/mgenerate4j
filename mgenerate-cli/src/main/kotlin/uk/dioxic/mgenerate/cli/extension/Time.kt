package uk.dioxic.mgenerate.cli.extension

import org.bson.BsonDateTime
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.toKotlinDuration


@ExperimentalTime
fun LocalDateTime.between(other: LocalDateTime): Duration = java.time.Duration.between(this, other).toKotlinDuration()

fun LocalDateTime.toEpochMillis(): Long = toInstant(ZoneOffset.UTC).toEpochMilli()

fun LocalDateTime.ofEpochMillis(millis: Long): LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC)

fun Date.toUtcLocalDateTime(): LocalDateTime = toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime()

fun BsonDateTime.toUtcLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC)