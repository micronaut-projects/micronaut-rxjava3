/*
 * Copyright 2017-2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.rxjava3.http.client.websockets;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.HttpClientConfiguration;
import io.micronaut.websocket.WebSocketClient;
import io.reactivex.rxjava3.core.Flowable;

import java.net.URI;
import java.net.URL;
import java.util.Map;

/**
 * Specialization of the {@link WebSocketClient} interface for RxJava 3.
 *
 * @author James Kleeh
 * @since 2.1.0
 * @see WebSocketClient
 */
public interface Rx3WebSocketClient extends WebSocketClient {

    @Override
    <T extends AutoCloseable> Flowable<T> connect(Class<T> clientEndpointType, MutableHttpRequest<?> request);

    /**
     * {@inheritDoc}
     */
    @Override
    default <T extends AutoCloseable> Flowable<T> connect(Class<T> clientEndpointType, URI uri) {
        return (Flowable<T>) WebSocketClient.super.connect(clientEndpointType, uri);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    <T extends AutoCloseable> Flowable<T> connect(Class<T> clientEndpointType, Map<String, Object> parameters);

    @Override
    default <T extends AutoCloseable> Flowable<T> connect(Class<T> clientEndpointType, String uri) {
        return (Flowable<T>) WebSocketClient.super.connect(clientEndpointType, uri);
    }

    /**
     * Create a new {@link Rx3WebSocketClient}.
     * Note that this method should only be used outside of the context of a Micronaut application.
     * The returned {@link Rx3WebSocketClient} is not subject to dependency injection.
     * The creator is responsible for closing the client to avoid leaking connections.
     * Within a Micronaut application use {@link jakarta.inject.Inject} to inject a client instead.
     *
     * @param url The base URL
     * @return The client
     * @since 2.1.0
     */
    static Rx3WebSocketClient create(@Nullable URL url) {
        return new BridgedRx3WebSocketClient(WebSocketClient.create(url));
    }

    /**
     * Create a new {@link Rx3WebSocketClient} with the specified configuration. Note that this method should only be used
     * outside of the context of an application. Within Micronaut use {@link jakarta.inject.Inject} to inject a client instead
     *
     * @param url The base URL
     * @param configuration the client configuration
     * @return The client
     * @since 2.1.0
     */
    static Rx3WebSocketClient create(@Nullable URL url, @NonNull HttpClientConfiguration configuration) {
        return new BridgedRx3WebSocketClient(WebSocketClient.create(url, configuration));
    }
}
