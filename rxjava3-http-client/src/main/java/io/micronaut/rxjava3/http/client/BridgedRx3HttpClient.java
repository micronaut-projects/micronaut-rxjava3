package io.micronaut.rxjava3.http.client;

import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.io.buffer.ByteBuffer;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.RxStreamingHttpClient;
import io.micronaut.http.client.sse.RxSseClient;
import io.micronaut.http.sse.Event;
import io.reactivex.rxjava3.core.Flowable;

import java.util.Map;

/**
 * Internal bridge for the HTTP client.
 *
 * @author graemerocher
 * @since 1.0
 */
@Internal
class BridgedRx3HttpClient implements Rx3HttpClient, Rx3SseClient, Rx3StreamingHttpClient {

    private final RxHttpClient rxHttpClient;

    public BridgedRx3HttpClient(RxHttpClient rxHttpClient) {
        this.rxHttpClient = rxHttpClient;
    }

    @Override
    public BlockingHttpClient toBlocking() {
        return null;
    }

    @Override
    public <I, O, E> Flowable<HttpResponse<O>> exchange(HttpRequest<I> request, Argument<O> bodyType, Argument<E> errorType) {
        return RxJavaBridge.toV3Flowable(rxHttpClient.exchange(request, bodyType, errorType));
    }

    @Override
    public <I, O> Flowable<HttpResponse<O>> exchange(HttpRequest<I> request, Argument<O> bodyType) {
        return RxJavaBridge.toV3Flowable(rxHttpClient.exchange(request, bodyType));
    }

    @Override
    public <I, O, E> Flowable<O> retrieve(HttpRequest<I> request, Argument<O> bodyType, Argument<E> errorType) {
        return RxJavaBridge.toV3Flowable(rxHttpClient.retrieve(request, bodyType));
    }

    @Override
    public <I> Flowable<HttpResponse<ByteBuffer>> exchange(HttpRequest<I> request) {
        return RxJavaBridge.toV3Flowable(rxHttpClient.exchange(request));
    }

    @Override
    public Flowable<HttpResponse<ByteBuffer>> exchange(String uri) {
        return RxJavaBridge.toV3Flowable(rxHttpClient.exchange(uri));
    }

    @Override
    public <O> Flowable<HttpResponse<O>> exchange(String uri, Class<O> bodyType) {
        return RxJavaBridge.toV3Flowable(rxHttpClient.exchange(uri, bodyType));
    }

    @Override
    public <I, O> Flowable<HttpResponse<O>> exchange(HttpRequest<I> request, Class<O> bodyType) {
        return RxJavaBridge.toV3Flowable(rxHttpClient.exchange(request, bodyType));
    }

    @Override
    public <I, O> Flowable<O> retrieve(HttpRequest<I> request, Argument<O> bodyType) {
        return RxJavaBridge.toV3Flowable(rxHttpClient.retrieve(request, bodyType));
    }

    @Override
    public <I, O> Flowable<O> retrieve(HttpRequest<I> request, Class<O> bodyType) {
        return RxJavaBridge.toV3Flowable(rxHttpClient.retrieve(request, bodyType));
    }

    @Override
    public <I> Flowable<String> retrieve(HttpRequest<I> request) {
        return RxJavaBridge.toV3Flowable(rxHttpClient.retrieve(request));
    }

    @Override
    public Flowable<String> retrieve(String uri) {
        return RxJavaBridge.toV3Flowable(rxHttpClient.retrieve(uri));
    }

    @Override
    public boolean isRunning() {
        return rxHttpClient.isRunning();
    }

    @Override
    public <I> Flowable<Event<ByteBuffer<?>>> eventStream(HttpRequest<I> request) {
        return RxJavaBridge.toV3Flowable(((RxSseClient) rxHttpClient).eventStream(request));
    }

    @Override
    public <I, B> Flowable<Event<B>> eventStream(HttpRequest<I> request, Argument<B> eventType) {
        return RxJavaBridge.toV3Flowable(((RxSseClient) rxHttpClient).eventStream(request, eventType));
    }

    @Override
    public <I, B> Flowable<Event<B>> eventStream(HttpRequest<I> request, Class<B> eventType) {
        return RxJavaBridge.toV3Flowable(((RxSseClient) rxHttpClient).eventStream(request, eventType));
    }

    @Override
    public <B> Flowable<Event<B>> eventStream(String uri, Class<B> eventType) {
        return RxJavaBridge.toV3Flowable(((RxSseClient) rxHttpClient).eventStream(uri, eventType));
    }

    @Override
    public <B> Flowable<Event<B>> eventStream(String uri, Argument<B> eventType) {
        return RxJavaBridge.toV3Flowable(((RxSseClient) rxHttpClient).eventStream(uri, eventType));
    }

    @Override
    public <I> Flowable<ByteBuffer<?>> dataStream(HttpRequest<I> request) {
        return RxJavaBridge.toV3Flowable(((RxStreamingHttpClient) rxHttpClient).dataStream(request));
    }

    @Override
    public <I> Flowable<HttpResponse<ByteBuffer<?>>> exchangeStream(HttpRequest<I> request) {
        return RxJavaBridge.toV3Flowable(((RxStreamingHttpClient) rxHttpClient).exchangeStream(request));
    }

    @Override
    public <I> Flowable<Map<String, Object>> jsonStream(HttpRequest<I> request) {
        return RxJavaBridge.toV3Flowable(((RxStreamingHttpClient) rxHttpClient).jsonStream(request));
    }

    @Override
    public <I, O> Flowable<O> jsonStream(HttpRequest<I> request, Argument<O> type) {
        return RxJavaBridge.toV3Flowable(((RxStreamingHttpClient) rxHttpClient).jsonStream(request, type));
    }

    @Override
    public <I, O> Flowable<O> jsonStream(HttpRequest<I> request, Class<O> type) {
        return RxJavaBridge.toV3Flowable(((RxStreamingHttpClient) rxHttpClient).jsonStream(request, type));
    }
}
