from micronaut.context.annotation import Requires
from micronaut.http import MediaType
from micronaut.http.annotation import Controller, Get
from micronaut.http.sse import Event
from io.reactivex.rxjava3.core import Flowable

from .Message import Message


@Requires(property="spec.name", value="Rx3HttpClientTest")
@Controller("/hello")
class HelloController:

    @Get(produces=MediaType.TEXT_PLAIN)
    def hello(self) -> str:
        return "Hello World"

    @Get(value="/stream", produces=MediaType.APPLICATION_JSON_STREAM)
    def stream(self) -> Flowable[Message]:
        return Flowable.just(Message("Hello"), Message("World"))

    @Get(value="/events", produces=MediaType.TEXT_EVENT_STREAM)
    def events(self) -> Flowable[Event[str]]:
        return Flowable.just(Event.of("Hello"), Event.of("World"))
