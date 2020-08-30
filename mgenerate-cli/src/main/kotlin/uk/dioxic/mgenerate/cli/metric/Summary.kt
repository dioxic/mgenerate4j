package uk.dioxic.mgenerate.cli.metric

import uk.dioxic.mgenerate.cli.extension.alignWith
import kotlin.time.ExperimentalTime

class Summary(private val fields: List<Pair<String, Any>>) {
    data class SummaryFormat(val separator: String, val spacing: Int) {
        companion object {
            val PIPED = SummaryFormat(" | ", 0)
            val SPACED = SummaryFormat("", 5)
        }
    }

    fun add(prefix: List<Pair<String, Any>>, suffix: List<Pair<String, Any>>) = Summary(
            listOf(prefix, fields, suffix).flatten()
    )

    @ExperimentalTime
    fun headerString(format: SummaryFormat) = headerString(format.separator, format.spacing)

    @ExperimentalTime
    fun linebreakString(format: SummaryFormat, padChar: Char = '-') = linebreakString(format.separator, format.spacing, padChar)

    @ExperimentalTime
    fun valueString(format: SummaryFormat) = valueString(format.separator, format.spacing)

    @ExperimentalTime
    fun headerString(separator: String, spacing: Int) = fields
            .joinToString(separator) { alignWith(it.first, it.second.toString(), spacing) }

    @ExperimentalTime
    fun linebreakString(separator: String, spacing: Int, padChar: Char = '-') = fields
            .joinToString(separator) { "".padEnd(maxOf(it.first.length, it.second.toString().length) + spacing, padChar) }

    @ExperimentalTime
    fun valueString(separator: String, spacing: Int) = fields
            .joinToString(separator) { alignWith(it.second.toString(), it.first, spacing) }
}