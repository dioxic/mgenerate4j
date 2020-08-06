package uk.dioxic.mgenerate.cli;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.reactivestreams.client.MongoCollection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoadRunner<T> {
    private final BulkWriteOptions writeOptions = new BulkWriteOptions().ordered(false);

    private final Generator<T> generator;
    private final MongoCollection<T> collection;
//    private final Schema<MODEL, SOURCE> schema;
    private final int concurrency;
    private final int batchSize;
    private final int recordsPerOperation;

//    public Mono<Long> load() {
//        Flux<SOURCE> generationFlux = generator.generate();
//
//        Flux<OperationMetrics> results = schema.writeModel(generationFlux)
//                .subscribeOn(Schedulers.newSingle("generator"))
//                .buffer(batchSize)
//                .flatMap(this::bulkWrite, concurrency)
//                .share();
//
//        monitor(results);
//
//        return results
//                .map(OperationMetrics::getOperationCount)
//                .collect(Collectors.summingLong(Long::longValue))
//                .doOnNext(count -> LOG.info("total document operations: {}", count));
//    }
//
//    private Mono<OperationMetrics> bulkWrite(List<WriteModel<MODEL>> batch) {
//        MetricBuilder builder = MetricBuilder.start(batchSize, recordsPerOperation);
//        return Mono.from(collection.bulkWrite(batch, writeOptions))
//                .map(builder::complete);
//    }
//
//    private void monitor(Flux<OperationMetrics> operationMetricsFlux) {
//        final AtomicInteger atomicOutputCount = new AtomicInteger();
//        final AtomicLong atomicParameterCount = new AtomicLong();
//
//        final AtomicReference<OperationMetrics> atomicMetric = new AtomicReference<>(OperationMetrics.ZERO);
//        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
//        final int seconds = 5;
//
//        Runnable monitorTask = () -> {
//            if (atomicOutputCount.getAndAccumulate(1, (x, y) -> (x + y) % 10) == 0) {
//                METRICS_LOG.info("ops/s\t\t\tparameters/s\t\tlatency (ms)\t\t% complete");
//            }
//            OperationMetrics windowMetrics = atomicMetric.getAndSet(OperationMetrics.ZERO);
//            long totalRecords = atomicParameterCount.addAndGet(windowMetrics.recordCount);
//
//            METRICS_LOG.info("{}\t\t\t{}\t\t\t{}\t\t\t{}",
//                    windowMetrics.getOperationCount() / seconds,
//                    windowMetrics.getRecordCount() / seconds,
//                    windowMetrics.getAverageLatency(),
//                    totalRecords / (generator.recordCount() / 100));
//        };
//
//        executor.scheduleAtFixedRate(monitorTask, 1, seconds, TimeUnit.SECONDS);
//
//        operationMetricsFlux
//                .doOnComplete(executor::shutdown)
//                .doOnError(err -> executor.shutdown())
//                .subscribe(metrics -> atomicMetric.accumulateAndGet(metrics, OperationMetrics::add));
//    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    static class MetricBuilder {
        private final long startMillis;
        private final int batchSize;
        private final int recordsPerBatch;

        static MetricBuilder start(int batchSize, int recordsPerOperation) {
            return new MetricBuilder(System.currentTimeMillis(), batchSize, recordsPerOperation * batchSize);
        }

        public OperationMetrics complete(BulkWriteResult writeResult) {
            return complete(batchSize);
        }

        public OperationMetrics complete(int operationCount) {
            long duration = System.currentTimeMillis() - startMillis;
            return new OperationMetrics(recordsPerBatch, operationCount, 1, duration);
        }
    }

    @Data
    static class OperationMetrics {
        private final long recordCount;
        private final long operationCount;
        private final long batchCount;
        private final long duration;

        public OperationMetrics add(OperationMetrics other) {
            return new OperationMetrics(
                    this.recordCount + other.recordCount,
                    this.operationCount + other.operationCount,
                    this.batchCount + other.batchCount,
                    this.duration + other.duration);
        }

        public static final OperationMetrics ZERO = new OperationMetrics(0, 0, 0,0);

        public long getAverageLatency() {
            return duration / batchCount;
        }
    }

}
