/*
 * Copyright 2016-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

@file:Suppress("unused")

package uk.dioxic.mgenerate.cli.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.fold
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.coroutineContext
import kotlin.math.roundToInt

/**
 * Is `true` when running in a nightly stress test mode.
 */
public val isStressTest = System.getProperty("stressTest")?.toBoolean() ?: false

public val stressTestMultiplierSqrt = if (isStressTest) 5 else 1

/**
 * Multiply various constants in stress tests by this factor, so that they run longer during nightly stress test.
 */
public val stressTestMultiplier = stressTestMultiplierSqrt * stressTestMultiplierSqrt

public val stressTestMultiplierCbrt = Math.cbrt(stressTestMultiplier.toDouble()).roundToInt()

public open class TestBase {

    private var actionIndex = AtomicInteger()
    private var finished = AtomicBoolean()
    private var error = AtomicReference<Throwable>()

    // Shutdown sequence
    private lateinit var threadsBefore: Set<Thread>
    private val uncaughtExceptions = Collections.synchronizedList(ArrayList<Throwable>())
    private var originalUncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null
    private val SHUTDOWN_TIMEOUT = 1_000L // 1s at most to wait per thread

    /**
     * Throws [IllegalStateException] like `error` in stdlib, but also ensures that the test will not
     * complete successfully even if this exception is consumed somewhere in the test.
     */
    @Suppress("ACTUAL_FUNCTION_WITH_DEFAULT_ARGUMENTS")
    public fun error(message: Any, cause: Throwable? = null): Nothing {
        throw makeError(message, cause)
    }

    private fun makeError(message: Any, cause: Throwable? = null): IllegalStateException =
            IllegalStateException(message.toString(), cause).also {
                setError(it)
            }

    private fun setError(exception: Throwable) {
        error.compareAndSet(null, exception)
    }

    private fun printError(message: String, cause: Throwable) {
        setError(cause)
        println("$message: $cause")
        cause.printStackTrace(System.out)
        println("--- Detected at ---")
        Throwable().printStackTrace(System.out)
    }

    /**
     * Throws [IllegalStateException] when `value` is false like `check` in stdlib, but also ensures that the
     * test will not complete successfully even if this exception is consumed somewhere in the test.
     */
    public inline fun check(value: Boolean, lazyMessage: () -> Any) {
        if (!value) error(lazyMessage())
    }

    /**
     * Asserts that this invocation is `index`-th in the execution sequence (counting from one).
     */
    public fun expect(index: Int) {
        val wasIndex = actionIndex.incrementAndGet()
        check(index == wasIndex) { "Expecting action index $index but it is actually $wasIndex" }
    }

    /**
     * Asserts that this line is never executed.
     */
    public fun expectUnreached() {
        error("Should not be reached")
    }

    /**
     * Asserts that this it the last action in the test. It must be invoked by any test that used [expect].
     */
    public fun finish(index: Int) {
        expect(index)
        check(!finished.getAndSet(true)) { "Should call 'finish(...)' at most once" }
    }

    /**
     * Asserts that [finish] was invoked
     */
    public fun ensureFinished() {
        require(finished.get()) { "finish(...) should be caller prior to this check" }
    }

    public fun reset() {
        check(actionIndex.get() == 0 || finished.get()) { "Expecting that 'finish(...)' was invoked, but it was not" }
        actionIndex.set(0)
        finished.set(false)
    }

//    @BeforeEach
//    fun before() {
//        initPoolsBeforeTest()
//        threadsBefore = currentThreads()
//        originalUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
//        Thread.setDefaultUncaughtExceptionHandler { t, e ->
//            println("Exception in thread $t: $e") // The same message as in default handler
//            e.printStackTrace()
//            uncaughtExceptions.add(e)
//        }
//    }
//
//    @AfterEach
//    fun onCompletion() {
//        // onCompletion should not throw exceptions before it finishes all cleanup, so that other tests always
//        // start in a clear, restored state
//        if (actionIndex.get() != 0 && !finished.get()) {
//            makeError("Expecting that 'finish(${actionIndex.get() + 1})' was invoked, but it was not")
//        }
//        // Shutdown all thread pools
//        shutdownPoolsAfterTest()
//        // Check that that are now leftover threads
//        runCatching {
//            checkTestThreads(threadsBefore)
//        }.onFailure {
//            setError(it)
//        }
//        // Restore original uncaught exception handler
//        Thread.setDefaultUncaughtExceptionHandler(originalUncaughtExceptionHandler)
//        if (uncaughtExceptions.isNotEmpty()) {
//            makeError("Expected no uncaught exceptions, but got $uncaughtExceptions")
//        }
//        // The very last action -- throw error if any was detected
//        error.get()?.let { throw it }
//    }
//
//    fun initPoolsBeforeTest() {
//        CommonPool.usePrivatePool()
//        DefaultScheduler.usePrivateScheduler()
//    }
//
//    fun shutdownPoolsAfterTest() {
//        CommonPool.shutdown(SHUTDOWN_TIMEOUT)
//        DefaultScheduler.shutdown(SHUTDOWN_TIMEOUT)
//        DefaultExecutor.shutdown(SHUTDOWN_TIMEOUT)
//        CommonPool.restore()
//        DefaultScheduler.restore()
//    }

    @ExperimentalStdlibApi
    @Suppress("ACTUAL_WITHOUT_EXPECT", "ACTUAL_FUNCTION_WITH_DEFAULT_ARGUMENTS")
    public fun runTest(
            expected: ((Throwable) -> Boolean)? = null,
            unhandled: List<(Throwable) -> Boolean> = emptyList(),
            block: suspend CoroutineScope.() -> Unit
    ) {
        var exCount = 0
        var ex: Throwable? = null
        try {
            runBlocking(block = block, context = CoroutineExceptionHandler { _, e ->
                if (e is CancellationException) return@CoroutineExceptionHandler // are ignored
                exCount++
                when {
                    exCount > unhandled.size ->
                        printError("Too many unhandled exceptions $exCount, expected ${unhandled.size}, got: $e", e)
                    !unhandled[exCount - 1](e) ->
                        printError("Unhandled exception was unexpected: $e", e)
                }
            })
        } catch (e: Throwable) {
            ex = e
            if (expected != null) {
                if (!expected(e))
                    error("Unexpected exception: $e", e)
            } else
                throw e
        } finally {
            if (ex == null && expected != null) error("Exception was expected but none produced")
        }
        if (exCount < unhandled.size)
            error("Too few unhandled exceptions $exCount, expected ${unhandled.size}")
    }

    protected inline fun <reified T: Throwable> assertFailsWith(block: () -> Unit): T {
        val result = runCatching(block)
        assertThat(result.exceptionOrNull() is T).withFailMessage( "Expected ${T::class}, but had $result").isTrue
        return result.exceptionOrNull()!! as T
    }

    protected suspend fun currentDispatcher() = coroutineContext[ContinuationInterceptor]!!
}

public suspend inline fun hang(onCancellation: () -> Unit) {
    try {
        suspendCancellableCoroutine<Unit> { }
    } finally {
        onCancellation()
    }
}

public inline fun <reified T : Throwable> assertFailsWith(block: () -> Unit) {
    try {
        block()
        error("Should not be reached")
    } catch (e: Throwable) {
        assertThat(e is T).isTrue
    }
}

public suspend inline fun <reified T : Throwable> assertFailsWith(flow: Flow<*>) {
    try {
        flow.collect()
        fail<String>("Should be unreached")
    } catch (e: Throwable) {
        assertThat(e is T).withFailMessage("Expected exception ${T::class}, but had $e instead").isTrue
    }
}

public suspend fun Flow<Int>.sum() = fold(0) { acc, value -> acc + value }
public suspend fun Flow<Long>.longSum() = fold(0L) { acc, value -> acc + value }


// data is added to avoid stacktrace recovery because CopyableThrowable is not accessible from common modules
public class TestException(message: String? = null, private val data: Any? = null) : Throwable(message)
public class TestException1(message: String? = null, private val data: Any? = null) : Throwable(message)
public class TestException2(message: String? = null, private val data: Any? = null) : Throwable(message)
public class TestException3(message: String? = null, private val data: Any? = null) : Throwable(message)
public class TestCancellationException(message: String? = null, private val data: Any? = null) : CancellationException(message)
public class TestRuntimeException(message: String? = null, private val data: Any? = null) : RuntimeException(message)
public class RecoverableTestException(message: String? = null) : RuntimeException(message)
public class RecoverableTestCancellationException(message: String? = null) : CancellationException(message)

public fun wrapperDispatcher(context: CoroutineContext): CoroutineContext {
    val dispatcher = context[ContinuationInterceptor] as CoroutineDispatcher
    return object : CoroutineDispatcher() {
        override fun isDispatchNeeded(context: CoroutineContext): Boolean =
            dispatcher.isDispatchNeeded(context)
        override fun dispatch(context: CoroutineContext, block: Runnable) =
            dispatcher.dispatch(context, block)
    }
}

public suspend fun wrapperDispatcher(): CoroutineContext = wrapperDispatcher(coroutineContext)

class BadClass {
    override fun equals(other: Any?): Boolean = error("equals")
    override fun hashCode(): Int = error("hashCode")
    override fun toString(): String = error("toString")
}
