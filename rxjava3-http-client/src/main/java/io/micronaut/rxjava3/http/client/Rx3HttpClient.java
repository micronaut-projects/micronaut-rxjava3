package io.micronaut.rxjava3.http.client;

import io.micronaut.core.io.buffer.ByteBuffer;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.reactivex.rxjava3.core.*;

/**
 * RxJava 3 variation of the {@link HttpClient} interface.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public interface Rx3HttpClient extends HttpClient {

    @Override
    default <I, O> Flowable<HttpResponse<O>> exchange(HttpRequest<I> request, Argument<O> bodyType) {
        return Flowable.fromPublisher(HttpClient.super.exchange(request, bodyType));
    }

    @Override
    <I, O, E> Flowable<HttpResponse<O>> exchange(HttpRequest<I> request, Argument<O> bodyType, Argument<E> errorType);

    @Override
    default <I, O, E> Flowable<O> retrieve(HttpRequest<I> request, Argument<O> bodyType, Argument<E> errorType) {
        return Flowable.fromPublisher(HttpClient.super.retrieve(request, bodyType, errorType));
    }

    @Override
    default <I> Flowable<HttpResponse<ByteBuffer>> exchange(HttpRequest<I> request) {
        return Flowable.fromPublisher(HttpClient.super.exchange(request));
    }

    @Override
    default Flowable<HttpResponse<ByteBuffer>> exchange(String uri) {
        return Flowable.fromPublisher(HttpClient.super.exchange(uri));
    }

    @Override
    default <O> Flowable<HttpResponse<O>> exchange(String uri, Class<O> bodyType) {
        return Flowable.fromPublisher(HttpClient.super.exchange(uri, bodyType));
    }

    @Override
    default <I, O> Flowable<HttpResponse<O>> exchange(HttpRequest<I> request, Class<O> bodyType) {
        return Flowable.fromPublisher(HttpClient.super.exchange(request, bodyType));
    }

    @Override
    default <I, O> Flowable<O> retrieve(HttpRequest<I> request, Argument<O> bodyType) {
        return Flowable.fromPublisher(HttpClient.super.retrieve(request, bodyType));
    }

    @Override
    default <I, O> Flowable<O> retrieve(HttpRequest<I> request, Class<O> bodyType) {
        return retrieve(
                request,
                Argument.of(bodyType),
                DEFAULT_ERROR_TYPE
        );
    }

    @Override
    default <I> Flowable<String> retrieve(HttpRequest<I> request) {
        return retrieve(
                request,
                Argument.STRING,
                DEFAULT_ERROR_TYPE
        );
    }

    @Override
    default Flowable<String> retrieve(String uri) {
        return retrieve(
                HttpRequest.GET(uri),
                Argument.STRING,
                DEFAULT_ERROR_TYPE
        );
    }

}
