package io.micronaut.rxjava3.docs;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
record Message(String text) {
}
