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
package io.micronaut.rxjava3.http.client;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.io.buffer.ByteBuffer;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.HttpClientConfiguration;
import io.reactivex.rxjava3.core.*;

import java.net.URL;

/**
 * RxJava 3 variation of the {@link HttpClient} interface.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public interface Rx3HttpClient extends HttpClient {

    @Override
    default <I, O> Flowable<HttpResponse<O>> exchange(@NonNull HttpRequest<I> request, @NonNull Argument<O> bodyType) {
        return Flowable.fromPublisher(HttpClient.super.exchange(request, bodyType));
    }

    @Override
    <I, O, E> Flowable<HttpResponse<O>> exchange(@NonNull HttpRequest<I> request, @NonNull Argument<O> bodyType, @NonNull Argument<E> errorType);

    @Override
    default <I, O, E> Flowable<O> retrieve(@NonNull HttpRequest<I> request, @NonNull Argument<O> bodyType, @NonNull Argument<E> errorType) {
        return Flowable.fromPublisher(HttpClient.super.retrieve(request, bodyType, errorType));
    }

    @Override
    default <I> Flowable<HttpResponse<ByteBuffer>> exchange(@NonNull HttpRequest<I> request) {
        return Flowable.fromPublisher(HttpClient.super.exchange(request));
    }

    @Override
    default Flowable<HttpResponse<ByteBuffer>> exchange(@NonNull String uri) {
        return Flowable.fromPublisher(HttpClient.super.exchange(uri));
    }

    @Override
    default <O> Flowable<HttpResponse<O>> exchange(@NonNull String uri, @NonNull Class<O> bodyType) {
        return Flowable.fromPublisher(HttpClient.super.exchange(uri, bodyType));
    }

    @Override
    default <I, O> Flowable<HttpResponse<O>> exchange(@NonNull HttpRequest<I> request, @NonNull Class<O> bodyType) {
        return Flowable.fromPublisher(HttpClient.super.exchange(request, bodyType));
    }

    @Override
    default <I, O> Flowable<O> retrieve(@NonNull HttpRequest<I> request, @NonNull Argument<O> bodyType) {
        return Flowable.fromPublisher(HttpClient.super.retrieve(request, bodyType));
    }

    @Override
    default <I, O> Flowable<O> retrieve(@NonNull HttpRequest<I> request, @NonNull Class<O> bodyType) {
        return retrieve(
                request,
                Argument.of(bodyType),
                DEFAULT_ERROR_TYPE
        );
    }

    @Override
    default <I> Flowable<String> retrieve(@NonNull HttpRequest<I> request) {
        return retrieve(
                request,
                Argument.STRING,
                DEFAULT_ERROR_TYPE
        );
    }

    @Override
    default Flowable<String> retrieve(@NonNull String uri) {
        return retrieve(
                HttpRequest.GET(uri),
                Argument.STRING,
                DEFAULT_ERROR_TYPE
        );
    }

    /**
     * Create a new {@link Rx3HttpClient}.
     * Note that this method should only be used outside of the context of a Micronaut application.
     * The returned {@link Rx3HttpClient} is not subject to dependency injection.
     * The creator is responsible for closing the client to avoid leaking connections.
     * Within a Micronaut application use {@link jakarta.inject.Inject} to inject a client instead.
     *
     * @param url The base URL
     * @return The client
     * @since 2.1.0
     */
    static Rx3HttpClient create(@Nullable URL url) {
        return new BridgedRx3HttpClient(HttpClient.create(url));
    }

    /**
     * Create a new {@link Rx3HttpClient} with the specified configuration. Note that this method should only be used
     * outside of the context of an application. Within Micronaut use {@link jakarta.inject.Inject} to inject a client instead
     *
     * @param url The base URL
     * @param configuration the client configuration
     * @return The client
     * @since 2.1.0
     */
    static Rx3HttpClient create(@Nullable URL url, @NonNull HttpClientConfiguration configuration) {
        return new BridgedRx3HttpClient(HttpClient.create(url, configuration));
    }
}
