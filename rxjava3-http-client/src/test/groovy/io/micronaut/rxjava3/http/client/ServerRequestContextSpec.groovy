package io.micronaut.rxjava3.http.client

import io.micronaut.context.annotation.Property
import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.core.io.buffer.ByteBuffer
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.context.ServerRequestContext
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import jakarta.inject.Inject
import jakarta.inject.Named
import org.reactivestreams.Publisher
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.charset.Charset
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService

@MicronautTest
@Property(name = 'micronaut.executors.io.type', value =  'FIXED')
@Property(name = 'micronaut.executors.io.nThreads', value =  '2')
class ServerRequestContextSpec extends Specification {

    @Inject TestClient testClient

    @Unroll
    void "test server request context is available for #method"() {
        expect:
        testClient."$method"() == uri

        where:
        method                      | uri
        "rxjavaSingle"              | '/test-context/rxjava-single'
        "rxjavaSingleScheduled"     | '/test-context/rxjava-single-scheduled'
        "rxjavaFlowable"            | '/test-context/rxjava-flowable'
        "rxjavaFlowableScheduled"   | '/test-context/rxjava-flowable-scheduled'
    }

    @Unroll
    void "test server request context is available in nested client call for #method"() {
        given:
        def expected = "pong : "+uri

        expect:
        testClient."$method"().blockingGet() == expected

        where:
        method                      | uri
        "rxjavaSingleClient"        | '/test-context/rxjava-single-client'
        "rxjavaFlowableClient"      | '/test-context/rxjava-flowable-client'
    }

    @Client('/test-context')
    @Consumes(MediaType.TEXT_PLAIN)
    static interface TestClient {

        @Get("/rxjava-single")
        String rxjavaSingle()

        @Get("/rxjava-single-scheduled")
        String rxjavaSingleScheduled()

        @Get("/rxjava-flowable")
        String rxjavaFlowable()

        @Get("/rxjava-flowable-scheduled")
        String rxjavaFlowableScheduled()

        @Get("/ping")
        Single<String> ping()

        @Get("/ping")
        Flowable<ByteBuffer> pingMany()

        @Get("/rxjava-single-client")
        Single<String> rxjavaSingleClient()

        @Get("/rxjava-flowable-client")
        Single<String> rxjavaFlowableClient()
    }

    @Controller('/test-context')
    @Produces(MediaType.TEXT_PLAIN)
    static class TestContextController {

        @Inject
        @Named(TaskExecutors.IO)
        ExecutorService executorService

        @Inject
        TestClient pingClient

        @Get("/ping")
        String ping() {
            return "pong"
        }

        @Get("/rxjava-single")
        @SingleResult
        Publisher<String> rxjavaSingle() {
            Single.fromCallable(new Callable<String>() {
                @Override
                String call() throws Exception {
                    HttpRequest<?> request = ServerRequestContext.currentRequest().orElseThrow { -> new RuntimeException("no request") }
                    request.uri
                }
            }).toFlowable()
        }

        @Get("/rxjava-single-scheduled")
        @SingleResult
        Publisher<String> rxjavaSingleScheduled() {
            Single.fromCallable(new Callable<String>() {
                @Override
                String call() throws Exception {
                    HttpRequest<?> request = ServerRequestContext.currentRequest().orElseThrow { -> new RuntimeException("no request") }
                    request.uri
                }
            }).subscribeOn(Schedulers.computation()).toFlowable()
        }

        @Get("/rxjava-flowable")
        @SingleResult
        Publisher<String> rxjavaFlowable() {
            Flowable.fromCallable(new Callable<String>() {
                @Override
                String call() throws Exception {
                    HttpRequest<?> request = ServerRequestContext.currentRequest().orElseThrow { -> new RuntimeException("no request") }
                    request.uri
                }
            })
        }

        @Get("/rxjava-flowable-scheduled")
        @SingleResult
        Publisher<String> rxjavaFlowableScheduled() {
            Flowable.fromCallable(new Callable<String>() {
                @Override
                String call() throws Exception {
                    HttpRequest<?> request = ServerRequestContext.currentRequest().orElseThrow { -> new RuntimeException("no request") }
                    request.uri
                }
            }).subscribeOn(Schedulers.computation())
        }

        @Get("/rxjava-single-client")
        @SingleResult
        Publisher<String> rxjavaSingleClient() {
            pingClient.ping().map(response -> {
                HttpRequest<?> request = ServerRequestContext.currentRequest().orElseThrow { -> new RuntimeException("no request") }
                return response +" : "+request.uri
            }).toFlowable()
        }

        @Get("/rxjava-flowable-client")
        @SingleResult
        Publisher<String> rxjavaFlowableClient() {
            pingClient.pingMany()
                    .map(chunk -> {
                        return chunk.toString(Charset.defaultCharset())
                    })
                    .toList()
                    .flatMapPublisher(response -> {
                        ServerRequestContext.currentRequest()
                        .map(request -> Flowable.just(response.get(0) +" : "+request.uri))
                        .orElse(Flowable.error(() -> {
                            return new IllegalStateException()
                        }))
                    })
        }
    }
}
