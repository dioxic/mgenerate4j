package uk.dioxic.mgenerate.cli.extension

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.*
import uk.dioxic.mgenerate.cli.metric.Metric
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
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
                                 loggingInterval: Duration): SendChannel<Metric> {
    val metricChannel = Channel<Metric>(capacity)

    launch {
        val spacing = 5
        val headerInterval = 10
        var counter = 0

        metricChannel
                .receiveAsFlow()
                .runningReduce { accumulator, value -> accumulator + value }
                .conflate()
                .onEach {
                    delay(loggingInterval)
                }
                .runningDifference { lastValue, value -> value - lastValue }
                .collect {
                    if (counter++ % headerInterval == 0) {
                        println(it.summaryHeader.printAlignWith(spacing, it.summary(totalExpected)))
                    }
                    println(it.summary(totalExpected).printAlignWith(spacing, it.summaryHeader))
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
fun <T> CoroutineScope.launchWorker(batchSize: Int,
                                    inputChannel: ReceiveChannel<List<T>>,
                                    metricChannel: SendChannel<Metric>,
                                    consumer: (List<T>) -> Any): Job =
        launch {
            for (input in inputChannel) {
                val timedValue = measureTimedValue {
                    consumer(input)
                }
                metricChannel.send(Metric.create(timedValue, batchSize))
            }
        }

@ExperimentalTime
fun <T> CoroutineScope.launchWorkers(batchSize: Int,
                                     parallelism: Int,
                                     inputChannel: ReceiveChannel<List<T>>,
                                     metricChannel: SendChannel<Metric>,
                                     consumer: (List<T>) -> Any): List<Job> = (1..parallelism).map {
    launchWorker(batchSize, inputChannel, metricChannel) {
        consumer(it)
    }
}