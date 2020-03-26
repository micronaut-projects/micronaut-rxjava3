package io.micronaut.rxjava3.http.client;

import io.micronaut.core.io.buffer.ByteBuffer;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.sse.SseClient;
import io.micronaut.http.sse.Event;
import io.reactivex.rxjava3.core.Flowable;

/**
 * RxJava 3 variation of the {@link SseClient} interface.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public interface Rx3SseClient extends SseClient {
    @Override
    <I> Flowable<Event<ByteBuffer<?>>> eventStream(HttpRequest<I> request);

    @Override
    <I, B> Flowable<Event<B>> eventStream(HttpRequest<I> request, Argument<B> eventType);

    @Override
    <I, B> Flowable<Event<B>> eventStream(HttpRequest<I> request, Class<B> eventType);

    @Override
    <B> Flowable<Event<B>> eventStream(String uri, Class<B> eventType);

    @Override
    <B> Flowable<Event<B>> eventStream(String uri, Argument<B> eventType);
}
