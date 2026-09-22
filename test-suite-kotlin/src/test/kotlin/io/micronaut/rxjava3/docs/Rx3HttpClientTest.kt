package io.micronaut.rxjava3.docs

import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.sse.Event
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

// tag::imports[]
import io.micronaut.http.client.annotation.Client
import io.micronaut.rxjava3.http.client.Rx3HttpClient
import io.micronaut.rxjava3.http.client.Rx3StreamingHttpClient
import io.micronaut.rxjava3.http.client.Rx3SseClient
import jakarta.inject.Inject
// end::imports[]

@Property(name = "spec.name", value = "Rx3HttpClientTest")
@MicronautTest
class Rx3HttpClientTest {

    // tag::clients[]
    @Inject @field:Client("/") lateinit var httpClient: Rx3HttpClient // <1>
    @Inject @field:Client("/") lateinit var sseClient: Rx3SseClient // <2>
    @Inject @field:Client("/") lateinit var streamingClient: Rx3StreamingHttpClient // <3>
    // end::clients[]

    @Test
    fun theRegularClientReturnsRxJavaTypes() {
        // tag::retrieve[]
        val greeting = httpClient.retrieve("/hello").blockingFirst()
        // end::retrieve[]
        assertEquals("Hello World", greeting)
    }

    @Test
    fun theStreamingClientReturnsAFlowableOfTheStreamedItems() {
        val words = streamingClient.jsonStream(HttpRequest.GET<Any>("/hello/stream"), Message::class.java)
            .map { it.text }
            .toList()
            .blockingGet()
        assertEquals(listOf("Hello", "World"), words)
    }

    @Test
    fun theSseClientReturnsAFlowableOfEvents() {
        val words = sseClient.eventStream(HttpRequest.GET<Any>("/hello/events"), String::class.java)
            .map { it.data }
            .toList()
            .blockingGet()
        assertEquals(listOf("Hello", "World"), words)
    }
}
