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

import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.io.buffer.ByteBuffer;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.reactivex.rxjava3.core.Flowable;

/**
 * Internal bridge for the HTTP client.
 *
 * @author graemerocher
 * @since 1.0
 */
@Internal
class BridgedRx3HttpClient implements Rx3HttpClient {

    private final HttpClient httpClient;

    /**
     * Default constructor.
     * @param httpClient HTTP Client
     */
    BridgedRx3HttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public BlockingHttpClient toBlocking() {
        return httpClient.toBlocking();
    }

    @Override
    public <I, O, E> Flowable<HttpResponse<O>> exchange(@NonNull HttpRequest<I> request, @NonNull Argument<O> bodyType, @NonNull Argument<E> errorType) {
        return Flowable.fromPublisher(httpClient.exchange(request, bodyType, errorType));
    }

    @Override
    public <I, O> Flowable<HttpResponse<O>> exchange(@NonNull HttpRequest<I> request, @NonNull Argument<O> bodyType) {
        return Flowable.fromPublisher(httpClient.exchange(request, bodyType));
    }

    @Override
    public <I, O, E> Flowable<O> retrieve(@NonNull HttpRequest<I> request, @NonNull Argument<O> bodyType, @NonNull Argument<E> errorType) {
        return Flowable.fromPublisher(httpClient.retrieve(request, bodyType));
    }

    @Override
    public <I> Flowable<HttpResponse<ByteBuffer>> exchange(@NonNull HttpRequest<I> request) {
        return Flowable.fromPublisher(httpClient.exchange(request));
    }

    @Override
    public Flowable<HttpResponse<ByteBuffer>> exchange(@NonNull String uri) {
        return Flowable.fromPublisher(httpClient.exchange(uri));
    }

    @Override
    public <O> Flowable<HttpResponse<O>> exchange(@NonNull String uri, @NonNull Class<O> bodyType) {
        return Flowable.fromPublisher(httpClient.exchange(uri, bodyType));
    }

    @Override
    public <I, O> Flowable<HttpResponse<O>> exchange(@NonNull HttpRequest<I> request, @NonNull Class<O> bodyType) {
        return Flowable.fromPublisher(httpClient.exchange(request, bodyType));
    }

    @Override
    public <I, O> Flowable<O> retrieve(@NonNull HttpRequest<I> request, @NonNull Argument<O> bodyType) {
        return Flowable.fromPublisher(httpClient.retrieve(request, bodyType));
    }

    @Override
    public <I, O> Flowable<O> retrieve(@NonNull HttpRequest<I> request, @NonNull Class<O> bodyType) {
        return Flowable.fromPublisher(httpClient.retrieve(request, bodyType));
    }

    @Override
    public <I> Flowable<String> retrieve(@NonNull HttpRequest<I> request) {
        return Flowable.fromPublisher(httpClient.retrieve(request));
    }

    @Override
    public Flowable<String> retrieve(@NonNull String uri) {
        return Flowable.fromPublisher(httpClient.retrieve(uri));
    }

    @Override
    public boolean isRunning() {
        return httpClient.isRunning();
    }

    @Override
    public void close() {
        httpClient.close();
    }

    @Override
    @NonNull
    public HttpClient start() {
        httpClient.start();
        return this;
    }

    @Override
    @NonNull
    public HttpClient stop() {
        httpClient.stop();
        return this;
    }
}
