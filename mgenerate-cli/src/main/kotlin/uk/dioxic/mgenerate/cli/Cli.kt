package uk.dioxic.mgenerate.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import uk.dioxic.mgenerate.cli.command.Generate
import uk.dioxic.mgenerate.cli.command.Load

class Cli : CliktCommand() {
    override fun run() = Unit
}

fun main(args: Array<String>) = Cli()
        .subcommands(Generate(), Load())
        .main(args)