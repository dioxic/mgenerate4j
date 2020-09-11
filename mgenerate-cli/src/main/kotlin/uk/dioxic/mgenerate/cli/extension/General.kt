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
import uk.dioxic.mgenerate.core.VariableCache
import uk.dioxic.mgenerate.core.codec.MgenDocumentCodec
import uk.dioxic.mgenerate.core.codec.TemplateCodec
import java.io.Writer
import java.util.concurrent.TimeUnit
import kotlin.math.round
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue
import kotlin.time.toDuration

fun MongoClientSettings.Builder.applyTemplateCodecRegistry(): MongoClientSettings.Builder {
    return codecRegistry(CodecRegistries.fromRegistries(CodecRegistries.fromCodecs(TemplateCodec()), MgenDocumentCodec.getCodecRegistry()))
}

fun templateOf(value: String): Template = if (value.startsWith("{")) Template.parse(value) else Template.from(value)

enum class OutputType(private val outputMode: JsonMode) {
    PRETTY(JsonMode.RELAXED),
    ARRAY(JsonMode.EXTENDED),
    NEWLINE(JsonMode.EXTENDED);

    fun jsonWriterSettings(outputMode: JsonMode = this.outputMode): JsonWriterSettings = JsonWriterSettings.builder()
            .indent(this == PRETTY)
            .outputMode(outputMode)
            .build()

    fun isArray() = this == ARRAY
}

fun <T> Writer.writeJson(values: Iterable<T>, codec: Codec<T>, jws: JsonWriterSettings, arrayOutput: Boolean = false) =
        writeJson(values.iterator(), codec, jws, arrayOutput)

fun <T> Writer.writeJson(values: Sequence<T>, codec: Codec<T>, jws: JsonWriterSettings, arrayOutput: Boolean = false) =
        writeJson(values.iterator(), codec, jws, arrayOutput)

fun <T> Writer.writeJson(values: Iterator<T>, codec: Codec<T>, jws: JsonWriterSettings, arrayOutput: Boolean = false) {
    if (arrayOutput) append('[')

    var first = true

    for (doc in values) {
        when {
            first -> first = false
            arrayOutput -> append(",")
            else -> write(System.lineSeparator())
        }
        writeJson(doc, jws, codec)
    }

    if (arrayOutput) append(']')
}

fun <T> Writer.writeJson(value: T, jws: JsonWriterSettings, codec: Codec<T>) =
        codec.encode(JsonWriter(this, jws), value, EncoderContext.builder().build())

@ExperimentalTime
fun measureTimedResultMetric(batchSize: Int, block: () -> Any): ResultMetric {
    val timedValue = measureTimedValue { block() }
    return ResultMetric.create(timedValue.value).with(timedValue.duration, batchSize)
}

fun <T> lockVariableState(block: () -> T): T {
    VariableCache.lock()
    val res = block()
    VariableCache.unlock()
    return res
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

@ExperimentalTime
infix fun Duration.percentOf(divisor: Duration) = "${round(100 * (this / divisor))}%"

fun alignWith(outputString: String, alignmentString: String, spacing: Int, padChar: Char = ' ') =
        outputString.padEnd(spacing + (alignmentString.length.coerceAtLeast(outputString.length)), padChar)

@ExperimentalTime
fun Iterable<Duration>.percentile(percentile: Double) = asSequence().percentile(percentile)

@ExperimentalTime
fun Sequence<Duration>.percentile(percentile: Double) = StatUtils.percentile(map { it.inMilliseconds }.toList().toDoubleArray(), percentile).toDuration(TimeUnit.MILLISECONDS)

@ExperimentalTime
fun Array<out Duration>.percentile(percentile: Double) = asSequence().percentile(percentile)