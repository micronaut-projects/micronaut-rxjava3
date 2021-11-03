/*
 * Copyright 2017-2019 original authors
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
package io.micronaut.rxjava3.http.client.sse;

import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.io.buffer.ByteBuffer;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.sse.SseClient;
import io.micronaut.http.sse.Event;
import io.micronaut.rxjava3.http.client.Rx3SseClient;
import io.reactivex.rxjava3.core.Flowable;
import org.reactivestreams.Publisher;

/**
 * RxJava 2 bridge for the Server side events HTTP client.
 *
 * @author Sergio del Amo
 * @since 3.0.0
 */
@Internal
public class BridgedRx3SseClient implements Rx3SseClient, AutoCloseable  {

    private final SseClient sseClient;

    /**
     * Default constructor.
     * @param sseClient Server Sent Events HTTP Client
     */
    public BridgedRx3SseClient(SseClient sseClient) {
        this.sseClient = sseClient;
    }

    @Override
    public <I> Flowable<Event<ByteBuffer<?>>> eventStream(@NonNull HttpRequest<I> request) {
        return Flowable.fromPublisher(sseClient.eventStream(request));
    }

    @Override
    public <I, B> Flowable<Event<B>> eventStream(@NonNull HttpRequest<I> request, @NonNull Argument<B> eventType) {
        return Flowable.fromPublisher(sseClient.eventStream(request, eventType));
    }

    @Override
    public <I, B> Publisher<Event<B>> eventStream(@NonNull HttpRequest<I> request, @NonNull Argument<B> eventType, @NonNull Argument<?> errorType) {
        return Flowable.fromPublisher(sseClient.eventStream(request, eventType, errorType));
    }

    @Override
    public <I, B> Flowable<Event<B>> eventStream(@NonNull HttpRequest<I> request, @NonNull Class<B> eventType) {
        return Flowable.fromPublisher(sseClient.eventStream(request, eventType));
    }

    @Override
    public <B> Flowable<Event<B>> eventStream(@NonNull String uri, @NonNull Class<B> eventType) {
        return Flowable.fromPublisher(sseClient.eventStream(uri, eventType));
    }

    @Override
    public <B> Flowable<Event<B>> eventStream(@NonNull String uri, @NonNull Argument<B> eventType) {
        return Flowable.fromPublisher(sseClient.eventStream(uri, eventType));
    }

    @Override
    public void close() throws Exception {
        if (sseClient instanceof AutoCloseable) {
            ((AutoCloseable) sseClient).close();
        }
    }
}
