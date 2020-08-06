package uk.dioxic.mgenerate.cli.util

import org.junit.jupiter.api.Test
import uk.dioxic.mgenerate.cli.command.template.GenerateTemplateCommand
import uk.dioxic.mgenerate.cli.mixin.GenerateMixin
import uk.dioxic.mgenerate.core.Template
import java.nio.file.Paths

class GenerateTemplateTest {

    private val template = Template.parse("{ name: '\$name' }")

    @Test
    fun generateToConsoleWithPretty() {
        val mixin = GenerateMixin()
        val command = GenerateTemplateCommand()

        mixin.pretty = true
        command.setMixin(mixin)
        command.setTemplate(template)

        command.call()
    }

    @Test
    fun generateToConsoleAsArray() {
        val mixin = GenerateMixin()
        val command = GenerateTemplateCommand()

        mixin.array = true
        command.setMixin(mixin)
        command.setTemplate(template)

        command.call()
    }

    @Test
    fun generateToFile() {
        val mixin = GenerateMixin()
        val command = GenerateTemplateCommand()

        mixin.file = Paths.get("c:/temp/generate.json")
        mixin.number = 100
        command.setMixin(mixin)
        command.setTemplate(template)

        command.call()
    }

}