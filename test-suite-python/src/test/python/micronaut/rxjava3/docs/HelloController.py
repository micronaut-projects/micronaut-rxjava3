from micronaut.context.annotation import Requires
from micronaut.http import MediaType
from micronaut.http.annotation import Controller, Get
from micronaut.http.sse import Event
from org.reactivestreams import Publisher

from .Message import Message

try:
    from io.reactivex.rxjava3.core import Flowable
except ImportError:  # TODO(python): packages under `io.` other than `io.micronaut` cannot be imported at runtime
    from reactivex.rxjava3.core import Flowable


@Requires(property="spec.name", value="Rx3HttpClientTest")
@Controller("/hello")
class HelloController:

    @Get(produces=MediaType.TEXT_PLAIN)
    def hello(self) -> str:
        return "Hello World"

    @Get(value="/stream", produces=MediaType.APPLICATION_JSON_STREAM)
    def stream(self) -> Publisher[Message]:
        return Flowable.just(Message("Hello"), Message("World"))

    @Get(value="/events", produces=MediaType.TEXT_EVENT_STREAM)
    def events(self) -> Publisher[Event[str]]:
        return Flowable.just(Event.of("Hello"), Event.of("World"))
