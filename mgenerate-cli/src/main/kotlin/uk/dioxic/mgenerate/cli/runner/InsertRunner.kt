package uk.dioxic.mgenerate.cli.runner

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.InsertManyOptions
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import uk.dioxic.mgenerate.cli.extension.launchMonitor
import uk.dioxic.mgenerate.cli.extension.launchWorker
import kotlin.contracts.ExperimentalContracts
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

@ExperimentalCoroutinesApi
@ExperimentalContracts
@ExperimentalTime
fun <T> insert(init: InsertRunnerBuilder<T>.() -> Unit) {
    val builder = InsertRunnerBuilder<T>()
    builder.init()
    return builder.build().run()
}

class InsertRunnerBuilder<T> {
    private var number: Long? = null
    private var parallelism: Int? = null
    private var batchSize: Int? = null
    private var insertOptions: InsertManyOptions? = null
    private var collection: MongoCollection<T>? = null
    private var producer: (() -> T)? = null

    fun number(number: Long) = apply { this.number = number }
    fun parallelism(parallelism: Int) = apply { this.parallelism = parallelism }
    fun batchSize(batchSize: Int) = apply { this.batchSize = batchSize }
    fun insertOptions(options: InsertManyOptions) = apply { this.insertOptions = options }
    fun collection(collection: MongoCollection<T>) = apply { this.collection = collection }
    fun producer(producer: () -> T) = apply { this.producer = producer }

    private fun validate() {
        requireNotNull(number) { "number not set" }
        requireNotNull(parallelism) { "parallelism not set" }
        requireNotNull(insertOptions) { "insertOptions not set" }
        requireNotNull(batchSize) { "batchSize not set" }
        requireNotNull(collection) { "collection not set" }
        requireNotNull(producer) { "producer not set" }
    }

    fun build(): InsertRunner<T> {
        validate()
        return InsertRunner(number!!, parallelism!!, batchSize!!, insertOptions!!, collection!!, producer!!)
    }
}

class InsertRunner<T>(
        private val number: Long = 1L,
        private val parallelism: Int = 1,
        private val batchSize: Int = 1,
        private val insertOptions: InsertManyOptions = InsertManyOptions(),
        private val collection: MongoCollection<T>,
        private val producer: () -> T) : Runnable {

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @ExperimentalContracts
    override fun run() {
        runBlocking(Dispatchers.Default) {
//            val inputChannel = produce(capacity = batchSize * 2) {
//                repeatLong(number) {
//                    send(producer())
//                }
//            }

//            val batchChannel = Channel<List<T>>(Channel.BUFFERED)
//            launchBatcher(inputChannel, batchChannel, batchSize)

//            launch {
//                generateSequence { producer() }
//                        .take(number.toInt())
//                        .chunked(batchSize)
//                        .forEach { batchChannel.send(it) }
//            }

//            launch {
//                (1..number).asFlow()
//                        .map { producer() }
//                        .buffer(batchSize *  2)
//                        .batch(100)
//                        .collect { batchChannel.send(it) }
//            }

            val batchChannel = produce(capacity = parallelism) {
                generateSequence { producer() }
                        .take(number.toInt())
                        .chunked(batchSize)
                        .forEach { send(it) }
            }

            val metricChannel = launchMonitor(loggingInterval = 1.seconds)

            val jobs = (1..parallelism).map {
                launchWorker(batchChannel, metricChannel) {
                    collection.insertMany(it, insertOptions)
                }
            }

            launch {
                jobs.joinAll()
                metricChannel.close()
            }

        }
    }

}