package io.micronaut.rxjava3.docs

import io.micronaut.context.annotation.Requires
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.sse.Event
import io.reactivex.rxjava3.core.Flowable

@Requires(property = "spec.name", value = "Rx3HttpClientTest")
@Controller("/hello")
class HelloController {

    @Get(produces = MediaType.TEXT_PLAIN)
    String hello() {
        "Hello World"
    }

    @Get(value = "/stream", produces = MediaType.APPLICATION_JSON_STREAM)
    Flowable<Message> stream() {
        Flowable.just(new Message(text: "Hello"), new Message(text: "World"))
    }

    @Get(value = "/events", produces = MediaType.TEXT_EVENT_STREAM)
    Flowable<Event<String>> events() {
        Flowable.just(Event.of("Hello"), Event.of("World"))
    }
}
