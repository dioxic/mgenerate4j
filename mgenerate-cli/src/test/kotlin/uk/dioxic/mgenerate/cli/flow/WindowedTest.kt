package uk.dioxic.mgenerate.cli.flow

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import uk.dioxic.mgenerate.cli.extension.windowed

@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
class WindowedTest : TestBase() {

    private val flow = flow {
        emit(1)
        emit(2)
        emit(3)
        emit(4)
    }

    @Test
    fun `Throws IllegalArgumentException for window of size or step less than 1`() {
        assertFailsWith<IllegalArgumentException> { flow.windowed(0, 1, false) }
        assertFailsWith<IllegalArgumentException> { flow.windowed(-1, 2, false) }
        assertFailsWith<IllegalArgumentException> { flow.windowed(2, 0, false) }
        assertFailsWith<IllegalArgumentException> { flow.windowed(5, -2, false) }
    }

    @Test
    fun `No emissions with empty flow`() = runTest {
        assertThat(flowOf<Int>().windowed(2, 2, false).count()).isEqualTo(0)
    }

    @Test
    fun `Emits correct sum with overlapping non partial windows`() = runTest {
        assertThat(flow.windowed(3, 1, false) { window ->
            window.sum()
        }.sum()).isEqualTo(15)
    }

    @Test
    fun `Emits correct sum with overlapping partial windows`() = runTest {
        assertThat(flow.windowed(3, 2, true) { window ->
            window.sum()
        }.sum()).isEqualTo(13)
    }

    @Test
    fun `Emits correct number of overlapping windows for long sequence of overlapping partial windows`() = runTest {
        val elements = generateSequence(1) { it + 1 }.take(100)
        val flow = elements.asFlow().windowed(100, 1, true) { }
        assertThat(flow.count()).isEqualTo(100)
    }

    @Test
    fun `Emits correct sum with partial windows set apart`() = runTest {
        assertThat(flow.windowed(2, 3, true) { window ->
            window.sum()
        }.sum()).isEqualTo(7)
    }


    @Test
    fun testErrorCancelsUpstream() = runTest {
        val latch = Channel<Unit>()
        val flow = flow {
            coroutineScope {
                launch(start = CoroutineStart.ATOMIC) {
                    latch.send(Unit)
                    hang { expect(3) }
                }
                emit(1)
                expect(1)
                emit(2)
                expectUnreached()
            }
        }.windowed<Int, Int>(2, 3, false) {
            expect(2) // 2
            latch.receive()
            throw TestException()
        }.catch { emit(42) }

        assertThat(flow.single()).isEqualTo(42)
        finish(4)
    }
}