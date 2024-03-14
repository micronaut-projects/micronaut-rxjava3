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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.TypeHint;
import io.micronaut.core.convert.MutableConversionService;
import io.micronaut.core.convert.TypeConverterRegistrar;
import io.micronaut.core.propagation.PropagatedContext;
import io.reactivex.rxjava3.core.*;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Converters for RxJava 3.
 *
 * @author graemerocher
 * @since 1.0
 */
@TypeHint({
        Flowable.class,
        Maybe.class,
        Completable.class,
        Single.class,
        Observable.class
})
public class RxJava3ConverterRegistrar implements TypeConverterRegistrar {

    @Override
    public void register(MutableConversionService conversionService) {
        // Completable
        conversionService.addConverter(Completable.class, Publisher.class, Completable::toFlowable);
        conversionService.addConverter(Completable.class, Single.class, (completable) ->
                completable.toSingleDefault(new Object())
        );
        conversionService.addConverter(Completable.class, Maybe.class, Completable::toMaybe);
        conversionService.addConverter(Completable.class, Observable.class, Completable::toObservable);
        conversionService.addConverter(Object.class, Completable.class, (obj) -> Completable.complete());

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

        // Publisher
        conversionService.addConverter(
                Publisher.class, Flowable.class,
                publisher -> {
                    if (publisher instanceof Flowable) {
                        return (Flowable) publisher;
                    }
                    return ContextPropagatingPublisher.asFlowable(publisher);
                }
        );
        conversionService.addConverter(Publisher.class, Single.class, ContextPropagatingPublisher::asSingle);
        conversionService.addConverter(Publisher.class, Observable.class, ContextPropagatingPublisher::asObservable);
        conversionService.addConverter(Publisher.class, Maybe.class, ContextPropagatingPublisher::asMaybe);
        conversionService.addConverter(Publisher.class, Completable.class, ContextPropagatingPublisher::asCompletable);
    }

    private static class ContextPropagatingPublisher<T> implements Publisher<T> {

        private final PropagatedContext context;
        private final Publisher<? extends T> actual;

        private ContextPropagatingPublisher(Publisher<? extends T> actual) {
            this.context = PropagatedContext.find().orElse(null);
            this.actual = actual;
        }

        public static <T> Flowable<T> asFlowable(Publisher<? extends T> actual) {
            return Flowable.fromPublisher(new ContextPropagatingPublisher<>(actual));
        }

        public static <T> Single<T> asSingle(Publisher<? extends T> actual) {
            return Single.fromPublisher(new ContextPropagatingPublisher<>(actual));
        }

        public static <T> Observable<T> asObservable(Publisher<? extends T> actual) {
            return Observable.fromPublisher(new ContextPropagatingPublisher<>(actual));
        }

        public static <T> Maybe<T> asMaybe(Publisher<? extends T> actual) {
            return Maybe.fromPublisher(new ContextPropagatingPublisher<>(actual));
        }

        public static <T> Completable asCompletable(Publisher<? extends T> actual) {
            return Completable.fromPublisher(new ContextPropagatingPublisher<>(actual));
        }

        @Override
        public void subscribe(Subscriber<? super T> subscriber) {
            if (context == null) {
                actual.subscribe(subscriber);
            } else {
                executeInContext(context, () -> actual.subscribe(new Subscriber<T>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        executeInContext(context, () -> subscriber.onSubscribe(subscription));
                    }

                    @Override
                    public void onNext(T t) {
                        executeInContext(context, () -> subscriber.onNext(t));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        executeInContext(context, () -> subscriber.onError(throwable));
                    }

                    @Override
                    public void onComplete() {
                        executeInContext(context, subscriber::onComplete);
                    }
                }));
            }
        }

        private void executeInContext(@NonNull PropagatedContext context, @NonNull Runnable runnable) {
            try (PropagatedContext.Scope ignore = context.propagate()) {
                runnable.run();
            }
         }
    }
}
