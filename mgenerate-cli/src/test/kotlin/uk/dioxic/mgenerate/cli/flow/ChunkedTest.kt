package uk.dioxic.mgenerate.cli.flow

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import uk.dioxic.mgenerate.cli.extension.chunked

@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
class ChunkedTest: TestBase() {

    private val flow = flow {
        emit(1)
        emit(2)
        emit(3)
        emit(4)
    }

    @Test
    fun `Chunks correct number of emissions with possible partial window at the end`() = runBlockingTest {
        assertThat(flow.chunked(2).count()).isEqualTo(2)
        assertThat(flow.chunked(3).count()).isEqualTo(2)
        assertThat(flow.chunked(5).count()).isEqualTo(1)
    }

    @Test
    fun `Throws IllegalArgumentException for chunk of size less than 1`() {
        assertThrows<IllegalArgumentException> { flow.chunked(0) }
        assertThrows<IllegalArgumentException> { flow.chunked(-1) }
    }

    @Test
    fun `No emissions with empty flow`() = runBlockingTest {
        assertThat(flowOf<Int>().chunked(2).count()).isEqualTo(0)
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
        }.chunked<Int, Int>(2) {
            expect(2) // 2
            latch.receive()
            throw TestException()
        }.catch { emit(42) }

        assertEquals(42, flow.single())
        finish(4)
    }
}
