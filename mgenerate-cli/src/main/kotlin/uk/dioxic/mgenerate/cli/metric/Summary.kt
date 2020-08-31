package uk.dioxic.mgenerate.cli.metric

import uk.dioxic.mgenerate.cli.extension.alignWith
import uk.dioxic.mgenerate.cli.extension.isNotZeroOrEmpty
import kotlin.time.ExperimentalTime

@ExperimentalTime
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


    fun headerString(format: SummaryFormat) = headerString(format.separator, format.spacing)

    fun linebreakString(format: SummaryFormat, padChar: Char = '-') = linebreakString(format.separator, format.spacing, padChar)

    fun valueString(format: SummaryFormat) = valueString(format.separator, format.spacing)

    fun headerString(separator: String, spacing: Int) = fields
            .joinToString(separator) { alignWith(it.first, it.second.toString(), spacing) }

    fun linebreakString(separator: String, spacing: Int, padChar: Char = '-') = fields
            .joinToString(separator) { "".padEnd(maxOf(it.first.length, it.second.toString().length) + spacing, padChar) }

    fun valueString(separator: String, spacing: Int) = fields
            .joinToString(separator) { alignWith(it.second.toString(), it.first, spacing) }

    fun filterNonEmpty() = Summary(fields.filter { it.isNotZeroOrEmpty() })
}