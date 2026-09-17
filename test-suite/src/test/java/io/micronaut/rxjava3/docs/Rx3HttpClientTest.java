package io.micronaut.rxjava3.docs;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.sse.Event;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;

// tag::imports[]
import io.micronaut.http.client.annotation.Client;
import io.micronaut.rxjava3.http.client.Rx3HttpClient;
import io.micronaut.rxjava3.http.client.Rx3StreamingHttpClient;
import io.micronaut.rxjava3.http.client.Rx3SseClient;
import jakarta.inject.Inject;
// end::imports[]

import static org.junit.jupiter.api.Assertions.assertEquals;

@Property(name = "spec.name", value = "Rx3HttpClientTest")
@MicronautTest
class Rx3HttpClientTest {

    // tag::clients[]
    @Inject @Client("/") Rx3HttpClient httpClient; // <1>
    @Inject @Client("/") Rx3SseClient sseClient; // <2>
    @Inject @Client("/") Rx3StreamingHttpClient streamingClient; // <3>
    // end::clients[]

    @Test
    void theRegularClientReturnsRxJavaTypes() {
        // tag::retrieve[]
        String greeting = httpClient.retrieve("/hello").blockingFirst();
        // end::retrieve[]
        assertEquals("Hello World", greeting);
    }

    @Test
    void theStreamingClientReturnsAFlowableOfTheStreamedItems() {
        List<String> words = streamingClient.jsonStream(HttpRequest.GET("/hello/stream"), Message.class)
            .map(Message::text)
            .toList()
            .blockingGet();
        assertEquals(List.of("Hello", "World"), words);
    }

    @Test
    void theSseClientReturnsAFlowableOfEvents() {
        List<String> words = sseClient.eventStream(HttpRequest.GET("/hello/events"), String.class)
            .map(Event::getData)
            .toList()
            .blockingGet();
        assertEquals(List.of("Hello", "World"), words);
    }
}
