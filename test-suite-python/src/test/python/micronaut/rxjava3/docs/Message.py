from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable


@Serdeable
@dataclass
class Message:
    text: str | None = None
