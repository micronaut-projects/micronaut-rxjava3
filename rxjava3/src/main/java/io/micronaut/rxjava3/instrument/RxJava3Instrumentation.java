/*
 * Copyright 2017-2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.rxjava3.instrument;


import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.TypeHint;
import io.micronaut.core.propagation.PropagatedContext;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.flowables.ConnectableFlowable;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.parallel.ParallelFlowable;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * Provides a single point of entry for all instrumentations for RxJava 2.x.
 *
 * @author Graeme Rocher
 * @since 1.0
 */
@Requires(classes = {RxJavaPlugins.class, PropagatedContext.class})
@Context
@Internal
final class RxJava3Instrumentation {

    private Function<? super Runnable, ? extends Runnable> scheduleHandler;

    @PostConstruct
    void init() {
        scheduleHandler = RxJavaPlugins.getScheduleHandler();
        RxJavaPlugins.setScheduleHandler(runnable -> {
            if (scheduleHandler != null) {
                runnable = scheduleHandler.apply(runnable);
            }
            return PropagatedContext.wrapCurrent(runnable);
        });
    }

    @PreDestroy
    void removeInstrumentation() {
        RxJavaPlugins.setScheduleHandler(scheduleHandler);
    }

}
