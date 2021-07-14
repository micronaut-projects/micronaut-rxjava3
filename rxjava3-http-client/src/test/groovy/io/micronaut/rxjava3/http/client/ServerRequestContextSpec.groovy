package io.micronaut.rxjava3.http.client

import io.micronaut.context.annotation.Property
import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.context.ServerRequestContext
import io.micronaut.scheduling.TaskExecutors
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.reactivestreams.Publisher
import spock.lang.Specification
import spock.lang.Unroll

import jakarta.inject.Inject
import jakarta.inject.Named

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
        method          | uri
        "rxjava"        | '/test-context/rxjava'
    }

    @Client('/test-context')
    @Consumes(MediaType.TEXT_PLAIN)
    static interface TestClient {

        @Get("/rxjava")
        String rxjava()
    }

    @Controller('/test-context')
    @Produces(MediaType.TEXT_PLAIN)
    static class TestContextController {

        @Inject
        @Named(TaskExecutors.IO)
        ExecutorService executorService

        @Get("/rxjava")
        @SingleResult
        Publisher<String> rxjava() {
            Single.fromCallable(new Callable<String>() {
                @Override
                String call() throws Exception {
                    HttpRequest<?> request = ServerRequestContext.currentRequest().orElseThrow { -> new RuntimeException("no request") }
                    request.uri
                }
            }).subscribeOn(Schedulers.computation()).toFlowable()
        }
    }
}
