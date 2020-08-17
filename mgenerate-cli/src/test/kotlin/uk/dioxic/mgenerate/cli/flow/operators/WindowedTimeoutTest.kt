package uk.dioxic.mgenerate.cli.flow.operators

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import uk.dioxic.mgenerate.cli.extension.windowed
import uk.dioxic.mgenerate.cli.extension.windowedTimeout
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

@ExperimentalTime
@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
class WindowedTimeoutTest : TestBase() {

    private val flow = flow {
        emit(1)
        emit(2)
        emit(3)
        emit(4)
    }

    @Test
    fun `Throws IllegalArgumentException for window of with non-positive timeout, size or step`() {
        assertFailsWith<IllegalArgumentException> { flow.windowedTimeout(1.seconds, 0, 1, false) }
        assertFailsWith<IllegalArgumentException> { flow.windowedTimeout(1.seconds, -1, 2, false) }
        assertFailsWith<IllegalArgumentException> { flow.windowedTimeout(1.seconds, 2, 0, false) }
        assertFailsWith<IllegalArgumentException> { flow.windowedTimeout(1.seconds, 5, -2, false) }
        assertFailsWith<IllegalArgumentException> { flow.windowedTimeout(0.seconds, 1, 1, false) }
        assertFailsWith<IllegalArgumentException> { flow.windowedTimeout((-1).seconds, 1, 1, false) }
    }

    @Test
    fun `No emissions with empty flow`() = runTest {
        assertThat(flowOf<Int>().windowedTimeout(1.seconds, 2, 2, false).count()).isEqualTo(0)
    }

    @Test
    fun `Emits correct sum with overlapping non partial windows`() = runTest {
        assertThat(flow.windowedTimeout(1.seconds, 3, 1, false) { window ->
            window.sum()
        }.sum()).isEqualTo(15)
    }

    @Test
    fun `Emits correct sum with overlapping partial windows`() = runTest {
        assertThat(flow.windowedTimeout(1.seconds, 3, 2, true) { window ->
            window.sum()
        }.sum()).isEqualTo(13)
    }

    @Test
    fun `Emits correct number of overlapping windows for long sequence of overlapping partial windows`() = runTest {
        val elements = generateSequence(1) { it + 1 }.take(100)
        val flow = elements.asFlow().windowedTimeout(1.seconds, 100, 1, true) { }
        assertThat(flow.count()).isEqualTo(100)
    }

    @Test
    fun `Emits correct sum with partial windows set apart`() = runTest {
        assertThat(flow.windowedTimeout(1.seconds, 2, 3, true) { window ->
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
        }.windowedTimeout<Int, Int>(1.seconds, 2, 3, false) {
            expect(2)
            latch.receive()
            throw TestException()
        }.catch { emit(42) }

        assertThat(flow.single()).isEqualTo(42)
        finish(4)
    }
}