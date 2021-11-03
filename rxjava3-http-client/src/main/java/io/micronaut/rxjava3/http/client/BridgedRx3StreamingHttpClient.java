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
import io.micronaut.http.client.StreamingHttpClient;
import io.reactivex.rxjava3.core.Flowable;
import org.reactivestreams.Publisher;

import java.util.Map;

/**
 * Internal bridge for the HTTP client.
 *
 * @author graemerocher
 * @since 1.0
 */
@Internal
class BridgedRx3StreamingHttpClient extends BridgedRx3HttpClient implements Rx3StreamingHttpClient {

    private final StreamingHttpClient streamingHttpClient;

    /**
     * Default constructor.
     * @param streamingHttpClient Streaming HTTP Client
     */
    BridgedRx3StreamingHttpClient(StreamingHttpClient streamingHttpClient) {
        super(streamingHttpClient);
        this.streamingHttpClient = streamingHttpClient;
    }

    @Override
    public <I> Flowable<ByteBuffer<?>> dataStream(@NonNull HttpRequest<I> request) {
        return Flowable.fromPublisher(streamingHttpClient.dataStream(request));
    }

    @Override
    public <I> Publisher<ByteBuffer<?>> dataStream(@NonNull HttpRequest<I> request, @NonNull Argument<?> errorType) {
        return Flowable.fromPublisher(streamingHttpClient.dataStream(request, errorType));
    }

    @Override
    public <I> Flowable<HttpResponse<ByteBuffer<?>>> exchangeStream(@NonNull HttpRequest<I> request) {
        return Flowable.fromPublisher(streamingHttpClient.exchangeStream(request));
    }

    @Override
    public <I> Publisher<HttpResponse<ByteBuffer<?>>> exchangeStream(@NonNull HttpRequest<I> request, @NonNull Argument<?> errorType) {
        return Flowable.fromPublisher(streamingHttpClient.exchangeStream(request, errorType));
    }

    @Override
    public <I> Flowable<Map<String, Object>> jsonStream(@NonNull HttpRequest<I> request) {
        return Flowable.fromPublisher(streamingHttpClient.jsonStream(request));
    }

    @Override
    public <I, O> Flowable<O> jsonStream(@NonNull HttpRequest<I> request, @NonNull Argument<O> type) {
        return Flowable.fromPublisher(streamingHttpClient.jsonStream(request, type));
    }

    @Override
    public <I, O> Publisher<O> jsonStream(@NonNull HttpRequest<I> request, @NonNull Argument<O> type, @NonNull Argument<?> errorType) {
        return Flowable.fromPublisher(streamingHttpClient.jsonStream(request, type, errorType));
    }

    @Override
    public <I, O> Flowable<O> jsonStream(@NonNull HttpRequest<I> request, @NonNull Class<O> type) {
        return Flowable.fromPublisher(streamingHttpClient.jsonStream(request, type));
    }
}
