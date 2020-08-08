package uk.dioxic.mgenerate.cli.extension

import java.lang.StringBuilder

fun List<String>.printAlignWith(spacing: Int, other: List<String>): String {
    val sb = StringBuilder()

    forEachIndexed { idx, item ->
        sb.append(item.padEnd(spacing + (other[idx].length.coerceAtLeast(item.length)), ' '))
    }

    return sb.toString()
}
