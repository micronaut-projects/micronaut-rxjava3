package io.micronaut.rxjava3.http.client

import io.micronaut.rxjava3.http.client.proxy.Rx3ProxyHttpClient
import io.micronaut.rxjava3.http.client.websockets.Rx3WebSocketClient
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class RxJavaClientInjectSpec extends Specification {

    @Inject Rx3SseClient sseClient
    @Inject Rx3StreamingHttpClient streamingHttpClient
    @Inject Rx3HttpClient httpClient
    @Inject Rx3WebSocketClient webSocketClient
    @Inject Rx3ProxyHttpClient proxyHttpClient

    void "test clients are injected"() {
        expect:
        sseClient != null
        streamingHttpClient != null
        httpClient != null
        webSocketClient != null
        proxyHttpClient != null
    }
}
