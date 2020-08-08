package uk.dioxic.mgenerate.cli.extension

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@Suppress("UNCHECKED_CAST")
@ExperimentalCoroutinesApi
fun <T> Flow<T>.runningDifference(operation: suspend (lastValue: T, value: T) -> T): Flow<T> = flow {
    var last: T? = null

    collect { value ->
        last = if (last === null) {
            value
        } else {
            emit(operation(last as T, value))
            value
        }
    }
}

/**
 * Returns a flow which performs the given [action] on each value of the original flow periodically.
 */
fun <T> Flow<T>.onEachPeriodic(period: Int, action: suspend (T) -> Unit): Flow<T> = transform { value ->
    var counter = 0

    if (counter++ % period == 0) {
        action(value)
    }

    return@transform emit(value)
}

@ExperimentalCoroutinesApi
fun <T> Flow<T>.batch(batchSize: Int): Flow<List<T>> = flow {
    var outputList = ArrayList<T>(batchSize)

    onCompletion { if (outputList.isNotEmpty()) emit(outputList) }
    collect {
        outputList.add(it)
        if (outputList.size == batchSize) {
            emit(outputList)
            outputList = ArrayList(batchSize)
        }
    }
}