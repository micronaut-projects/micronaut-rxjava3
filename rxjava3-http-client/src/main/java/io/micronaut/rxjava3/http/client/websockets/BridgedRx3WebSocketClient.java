/*
 * Copyright 2017-2021 original authors
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

import io.micronaut.core.annotation.Internal;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.websocket.WebSocketClient;
import io.reactivex.rxjava3.core.Flowable;

import java.util.Map;

/**
 * RxJava 3 bridge for the {@link WebSocketClient}.
 *
 * @author James Kleeh
 * @since 2.1.0
 */
@Internal
class BridgedRx3WebSocketClient implements Rx3WebSocketClient {

    private final WebSocketClient webSocketClient;

    /**
     *
     * @param webSocketClient Websocket client
     */
    BridgedRx3WebSocketClient(WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    @Override
    public <T extends AutoCloseable> Flowable<T> connect(Class<T> clientEndpointType, MutableHttpRequest<?> request) {
        return Flowable.fromPublisher(webSocketClient.connect(clientEndpointType, request));
    }

    @Override
    public <T extends AutoCloseable> Flowable<T> connect(Class<T> clientEndpointType, Map<String, Object> parameters) {
        return Flowable.fromPublisher(webSocketClient.connect(clientEndpointType, parameters));
    }

    @Override
    public void close() {
        webSocketClient.close();
    }
}
