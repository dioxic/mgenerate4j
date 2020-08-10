package uk.dioxic.mgenerate.cli.extension

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.*
import uk.dioxic.mgenerate.cli.metric.ResultMetric
import uk.dioxic.mgenerate.cli.metric.summarise
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@ExperimentalCoroutinesApi
fun <T> CoroutineScope.launchBatcher(capacity: Int = Channel.BUFFERED,
                                     inputChannel: ReceiveChannel<T>,
                                     batchSize: Int): ReceiveChannel<List<T>> {
    val batchChannel = Channel<List<T>>(capacity)
    launch {
        var outputList = ArrayList<T>(batchSize)
        for (msg in inputChannel) {
            outputList.add(msg)
            if (outputList.size == batchSize) {
                batchChannel.send(outputList)
                outputList = ArrayList(batchSize)
            }
        }

        if (outputList.isNotEmpty()) {
            batchChannel.send(outputList)
        }

        batchChannel.close()
    }
    return batchChannel
}

@ExperimentalCoroutinesApi
fun <T, M> CoroutineScope.launchTransformer(capacity: Int = Channel.BUFFERED,
                                            inputChannel: ReceiveChannel<T>,
                                            transformer: (T) -> M): ReceiveChannel<M> {
    val outputChannel = Channel<M>(capacity)
    launch {
        for (msg in inputChannel) {
            outputChannel.send(transformer(msg))
        }

        outputChannel.close()
    }
    return outputChannel
}

@ExperimentalTime
@ExperimentalCoroutinesApi
fun CoroutineScope.launchMonitor(totalExpected: Long,
                                 capacity: Int = Channel.BUFFERED,
                                 loggingInterval: Duration): SendChannel<ResultMetric> {
    val metricChannel = Channel<ResultMetric>(capacity)

    launch {
        val headerInterval = 10
        var counter = 0
        val spacing = 5
        var totalActual: Long = 0

        metricChannel
                .receiveAsFlow()
                .onEach { totalActual += it.batchSize }
                .chunkedTimeout(loggingInterval, 50000) { it.summarise() }
                .collect {
                    val summaryFields = it.summaryFields
                            .filter {(_, value) -> isPositive(value) }
                            .toMutableList()
                    summaryFields.add("progress" to (totalActual percentOf totalExpected))
                    if (counter++ % headerInterval == 0) {
                        println(summaryFields.firstAligned(spacing))
                    }
                    println(summaryFields.secondAligned(spacing))
                }
    }

    return metricChannel
}

@ExperimentalCoroutinesApi
fun <E> CoroutineScope.launchProducer(capacity: Int = Channel.BUFFERED,
                                      number: Long,
                                      producer: () -> E): ReceiveChannel<E> =
        produce(capacity = capacity) {
            generateSequence { producer() }
                    .take(number.toInt())
                    .forEach { send(it) }
        }

@ExperimentalCoroutinesApi
fun <E> CoroutineScope.launchBatchProducer(capacity: Int = Channel.BUFFERED,
                                           batchSize: Int,
                                           number: Long,
                                           producer: () -> E): ReceiveChannel<List<E>> =
        produce(capacity = capacity) {
            generateSequence { producer() }
                    .take(number.toInt())
                    .chunked(batchSize)
                    .forEach { send(it) }
        }

@ExperimentalTime
fun <T> CoroutineScope.launchWorker(inputChannel: ReceiveChannel<List<T>>,
                                       metricChannel: SendChannel<ResultMetric>,
                                       consumer: (List<T>) -> Any): Job =
        launch {
            for (input in inputChannel) {
                val timedValue = measureTimedValue {
                    consumer(input)
                }

                metricChannel.send(ResultMetric.create(timedValue, input.size))
            }
        }

@ExperimentalTime
fun <T> CoroutineScope.launchWorkers(parallelism: Int,
                                        inputChannel: ReceiveChannel<List<T>>,
                                        metricChannel: SendChannel<ResultMetric>,
                                        consumer: (List<T>) -> Any): List<Job> = (1..parallelism).map {
    launchWorker(inputChannel, metricChannel) {
        consumer(it)
    }
}