package io.micronaut.rxjava3.http.client;

import io.micronaut.core.io.buffer.ByteBuffer;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.StreamingHttpClient;
import io.reactivex.rxjava3.core.Flowable;

import java.util.Map;

/**
 * RxJava 3 variation of the {@link StreamingHttpClient} interface.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public interface Rx3StreamingHttpClient extends StreamingHttpClient {

    @Override
    <I> Flowable<ByteBuffer<?>> dataStream(HttpRequest<I> request);

    @Override
    <I> Flowable<HttpResponse<ByteBuffer<?>>> exchangeStream(HttpRequest<I> request);

    @Override
    <I> Flowable<Map<String, Object>> jsonStream(HttpRequest<I> request);

    @Override
    <I, O> Flowable<O> jsonStream(HttpRequest<I> request, Argument<O> type);

    @Override
    <I, O> Flowable<O> jsonStream(HttpRequest<I> request, Class<O> type);
}
