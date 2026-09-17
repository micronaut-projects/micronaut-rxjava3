package io.micronaut.rxjava3.docs

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class Message(val text: String)
