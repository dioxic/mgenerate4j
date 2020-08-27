package uk.dioxic.mgenerate.cli.extension

import com.mongodb.MongoClientSettings
import org.apache.commons.math3.stat.StatUtils
import org.bson.codecs.Codec
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistries
import org.bson.json.JsonMode
import org.bson.json.JsonWriter
import org.bson.json.JsonWriterSettings
import uk.dioxic.mgenerate.cli.metric.ResultMetric
import uk.dioxic.mgenerate.core.Template
import uk.dioxic.mgenerate.core.codec.MgenDocumentCodec
import uk.dioxic.mgenerate.core.codec.TemplateCodec
import java.io.OutputStream
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue
import kotlin.time.toDuration

fun MongoClientSettings.Builder.applyTemplateCodecRegistry(): MongoClientSettings.Builder {
    return codecRegistry(CodecRegistries.fromRegistries(CodecRegistries.fromCodecs(TemplateCodec()), MgenDocumentCodec.getCodecRegistry()))
}

fun templateOf(value: String): Template = if (value.startsWith("{")) Template.parse(value) else Template.from(value)

enum class OutputType {
    PRETTY, ARRAY, NEWLINE
}

fun <T> Iterable<T>.writeToOutputStream(outputStream: OutputStream, outputType: OutputType, codec: Codec<T>) =
        this.iterator().writeToOutputStream(outputStream, outputType, codec)

fun <T> Sequence<T>.writeToOutputStream(outputStream: OutputStream, outputType: OutputType, codec: Codec<T>) =
        this.iterator().writeToOutputStream(outputStream, outputType, codec)

fun <T> Iterator<T>.writeToOutputStream(outputStream: OutputStream, outputType: OutputType, codec: Codec<T>) {
    val jws = JsonWriterSettings.builder()
            .indent(outputType == OutputType.PRETTY)
            .outputMode(JsonMode.RELAXED)
            .build()

    outputStream.bufferedWriter().use {
        if (outputType == OutputType.ARRAY) it.append('[')

        var first = true

        for (doc in this) {
            when {
                first -> first = false
                outputType == OutputType.ARRAY -> it.append(",")
                else -> it.newLine()
            }
            codec.encode(JsonWriter(it, jws), doc, EncoderContext.builder().build())
        }

        if (outputType == OutputType.ARRAY) it.append(']')
    }
}

@ExperimentalTime
fun measureTimedResultMetric(batchSize: Int, block: () -> Any): ResultMetric {
    val timedValue = measureTimedValue { block() }
    return ResultMetric.create(timedValue.value).with(timedValue.duration, batchSize)
}

@ExperimentalTime
fun isNotZeroOrEmpty(value: Any) = when (value) {
    is Long -> value > 0
    is Double -> value > 0
    is Float -> value > 0
    is Int -> value > 0
    is Short -> value > 0
    is Duration -> value.isPositive()
    is String -> value.isNotEmpty()
    is List<*> -> value.isNotEmpty()
    else -> true
}

@ExperimentalTime
fun Pair<String, Any>.isNotZeroOrEmpty() =
        when (val value = this.second) {
            is Long -> value > 0
            is Double -> value > 0
            is Float -> value > 0
            is Int -> value > 0
            is Short -> value > 0
            is Duration -> value.isPositive()
            is String -> value.isNotEmpty()
            is List<*> -> value.isNotEmpty()
            else -> true
        }

infix fun Int.percentOf(divisor: Int) = "${((this * 100) / divisor)}%"

infix fun Long.percentOf(divisor: Long) = "${((this * 100) / divisor)}%"

fun alignWith(outputString: String, alignmentString: String, spacing: Int, padChar: Char = ' ') =
        outputString.padEnd(spacing + (alignmentString.length.coerceAtLeast(outputString.length)), padChar)

@ExperimentalTime
fun Iterable<Duration>.percentile(percentile: Double) = asSequence().percentile(percentile)

@ExperimentalTime
fun Sequence<Duration>.percentile(percentile: Double) = StatUtils.percentile(map { it.inMilliseconds }.toList().toDoubleArray(), percentile).toDuration(TimeUnit.MILLISECONDS)

@ExperimentalTime
fun Array<out Duration>.percentile(percentile: Double) = asSequence().percentile(percentile)