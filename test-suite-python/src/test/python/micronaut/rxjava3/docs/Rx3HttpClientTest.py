from java.lang import String
from micronaut.context.annotation import Property
from micronaut.http import HttpRequest
from micronaut.test.extensions.junit5.annotation import MicronautTest
from org.junit.jupiter.api import Test

# tag::imports[]
from typing import Annotated

from jakarta.inject import Inject
from micronaut.http.client.annotation import Client
from micronaut.rxjava3.http.client import Rx3HttpClient, Rx3SseClient, Rx3StreamingHttpClient
# end::imports[]

from .Message import Message


@Property(name="spec.name", value="Rx3HttpClientTest")
@MicronautTest
class Rx3HttpClientTest:

    # tag::clients[]
    http_client: Annotated[Rx3HttpClient, Inject, Client("/")]  # <1>
    sse_client: Annotated[Rx3SseClient, Inject, Client("/")]  # <2>
    streaming_client: Annotated[Rx3StreamingHttpClient, Inject, Client("/")]  # <3>
    # end::clients[]

    @Test
    def the_regular_client_returns_rxjava_types(self) -> None:
        # tag::retrieve[]
        greeting = self.http_client.retrieve("/hello").blockingFirst()
        # end::retrieve[]
        assert greeting == "Hello World"

    @Test
    def the_streaming_client_returns_a_flowable_of_the_streamed_items(self) -> None:
        words = self.streaming_client.jsonStream(HttpRequest.GET("/hello/stream"), Message) \
            .map(lambda message: message.text) \
            .toList() \
            .blockingGet()
        assert list(words) == ["Hello", "World"]

    @Test
    def the_sse_client_returns_a_flowable_of_events(self) -> None:
        words = self.sse_client.eventStream(HttpRequest.GET("/hello/events"), String) \
            .map(lambda event: event.getData()) \
            .toList() \
            .blockingGet()
        assert list(words) == ["Hello", "World"]
