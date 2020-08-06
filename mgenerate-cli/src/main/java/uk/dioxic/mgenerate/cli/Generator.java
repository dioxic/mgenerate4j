package uk.dioxic.mgenerate.cli;

import reactor.core.publisher.Flux;

public interface Generator<T> {

    Flux<T> generate();

    Class<T> getModelClass();

}
