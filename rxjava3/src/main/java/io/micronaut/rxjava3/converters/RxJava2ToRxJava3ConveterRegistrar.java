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
import io.micronaut.core.convert.MutableConversionService;
import io.micronaut.core.convert.TypeConverterRegistrar;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.Completable;
import jakarta.inject.Singleton;

/**
 * Converters between RxJava 2 and  RxJava 3.
 *
 * @author Sergio del Amo
 * @since 2.0.0
 */
@Singleton
@Requires(classes = {
        io.reactivex.Flowable.class,
        io.reactivex.Maybe.class,
        io.reactivex.Single.class,
        io.reactivex.Completable.class,
        io.reactivex.Observable.class})
@BootstrapContextCompatible
public class RxJava2ToRxJava3ConveterRegistrar implements TypeConverterRegistrar {

    @Override
    public void register(MutableConversionService conversionService) {

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
    }
}
