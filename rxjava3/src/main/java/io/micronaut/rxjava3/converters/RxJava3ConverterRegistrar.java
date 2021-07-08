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
package io.micronaut.rxjava3.converters;

import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.micronaut.context.annotation.BootstrapContextCompatible;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.TypeHint;
import io.micronaut.core.convert.ConversionService;
import io.micronaut.core.convert.TypeConverterRegistrar;
import io.reactivex.rxjava3.core.*;
import org.reactivestreams.Publisher;
import jakarta.inject.Singleton;

/**
 * Converters for RxJava 3. Copied from {@link io.micronaut.reactive.rxjava2.converters.RxJavaConverterRegistrar}.
 *
 * @author graemerocher
 * @since 1.0
 */
@Singleton
@Requires(classes = Flowable.class)
@BootstrapContextCompatible
@TypeHint({
        Flowable.class,
        Maybe.class,
        Completable.class,
        Single.class,
        Observable.class
})
public class RxJava3ConverterRegistrar implements TypeConverterRegistrar {

    @SuppressWarnings("unchecked")
    @Override
    public void register(ConversionService<?> conversionService) {

        // Interop Maybe
        conversionService.addConverter(Maybe.class, io.reactivex.Maybe.class, RxJavaBridge::toV2Maybe);
        conversionService.addConverter(io.reactivex.Maybe.class, Maybe.class, RxJavaBridge::toV3Maybe);
        conversionService.addConverter(Maybe.class, io.reactivex.Flowable.class, maybe ->
                RxJavaBridge.toV2Maybe(maybe).toFlowable()
        );
        conversionService.addConverter(io.reactivex.Flowable.class, Maybe.class, flowable ->
                RxJavaBridge.toV3Flowable(flowable).firstElement()
        );
        conversionService.addConverter(Maybe.class, io.reactivex.Single.class, maybe ->
                RxJavaBridge.toV2Maybe(maybe).toSingle()
        );

        // Interop Single
        conversionService.addConverter(Single.class, io.reactivex.Single.class, RxJavaBridge::toV2Single);
        conversionService.addConverter(io.reactivex.Single.class, Single.class, RxJavaBridge::toV3Single);
        conversionService.addConverter(Single.class, io.reactivex.Flowable.class, single ->
                RxJavaBridge.toV2Single(single).toFlowable()
        );
        conversionService.addConverter(io.reactivex.Flowable.class, Single.class, flowable ->
                RxJavaBridge.toV3Flowable(flowable).firstOrError()
        );
        conversionService.addConverter(Single.class, io.reactivex.Maybe.class, single ->
                RxJavaBridge.toV2Single(single).toMaybe()
        );

        // Interop Flowable
        conversionService.addConverter(Flowable.class, io.reactivex.Flowable.class, RxJavaBridge::toV2Flowable);
        conversionService.addConverter(io.reactivex.Flowable.class, Flowable.class, RxJavaBridge::toV3Flowable);

        // Completable
        conversionService.addConverter(Completable.class, Publisher.class, Completable::toFlowable);
        conversionService.addConverter(Completable.class, Single.class, (completable) ->
                completable.toSingleDefault(new Object())
        );
        conversionService.addConverter(Completable.class, Maybe.class, Completable::toMaybe);
        conversionService.addConverter(Completable.class, Observable.class, Completable::toObservable);
        conversionService.addConverter(Object.class, Completable.class, (obj) -> Completable.complete());

        // Interop Completable
        conversionService.addConverter(Completable.class, io.reactivex.Single.class, completable ->
                RxJavaBridge.toV2Single(completable.toSingleDefault(new Object()))
        );
        conversionService.addConverter(Completable.class, io.reactivex.Maybe.class, completable ->
                RxJavaBridge.toV2Maybe(completable.toMaybe())
        );
        conversionService.addConverter(Completable.class, io.reactivex.Observable.class, completable ->
                RxJavaBridge.toV2Observable(completable.toObservable())
        );
        conversionService.addConverter(Completable.class, io.reactivex.Completable.class, RxJavaBridge::toV2Completable);
        conversionService.addConverter(io.reactivex.Completable.class, Completable.class, RxJavaBridge::toV3Completable);

        // Maybe
        conversionService.addConverter(Maybe.class, Publisher.class, Maybe::toFlowable);
        conversionService.addConverter(Maybe.class, Single.class, Maybe::toSingle);
        conversionService.addConverter(Maybe.class, Observable.class, Maybe::toObservable);
        conversionService.addConverter(Maybe.class, Completable.class, Completable::fromMaybe);
        conversionService.addConverter(Object.class, Maybe.class, Maybe::just);

        // Observable
        conversionService.addConverter(Observable.class, Publisher.class, observable ->
                observable.toFlowable(BackpressureStrategy.BUFFER)
        );
        conversionService.addConverter(Observable.class, Single.class, Observable::firstOrError);
        conversionService.addConverter(Observable.class, Maybe.class, Observable::firstElement);
        conversionService.addConverter(Observable.class, Completable.class, Completable::fromObservable);
        conversionService.addConverter(Object.class, Observable.class, o -> {
            if (o instanceof Iterable) {
                return Observable.fromIterable((Iterable) o);
            } else {
                return Observable.just(o);
            }
        });

        // Single
        conversionService.addConverter(Single.class, Publisher.class, Single::toFlowable);
        conversionService.addConverter(Single.class, Maybe.class, Single::toMaybe);
        conversionService.addConverter(Single.class, Observable.class, Single::toObservable);
        conversionService.addConverter(Single.class, Completable.class, Completable::fromSingle);
        conversionService.addConverter(Object.class, Single.class, Single::just);

        // Flowable
        conversionService.addConverter(Flowable.class, Single.class, Flowable::firstOrError);
        conversionService.addConverter(Flowable.class, Maybe.class, Flowable::firstElement);
        conversionService.addConverter(Flowable.class, Observable.class, Flowable::toObservable);
        conversionService.addConverter(Flowable.class, Completable.class, Completable::fromPublisher);
        conversionService.addConverter(Object.class, Flowable.class, o -> {
            if (o instanceof Iterable) {
                return Flowable.fromIterable((Iterable) o);
            } else {
                return Flowable.just(o);
            }
        });

    }
}
