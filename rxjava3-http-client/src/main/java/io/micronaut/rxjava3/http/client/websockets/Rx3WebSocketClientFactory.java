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

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.context.annotation.Secondary;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.client.HttpClientConfiguration;
import io.micronaut.http.client.LoadBalancer;
import io.micronaut.websocket.WebSocketClientRegistry;
import io.micronaut.inject.InjectionPoint;

/**
 * Factory interface for creating RxJava 3 {@link io.micronaut.websocket.WebSocketClient}.

 * @author James Kleeh
 * @since 2.1.0
 */
@Factory
public class Rx3WebSocketClientFactory {

    private final WebSocketClientRegistry<?> clientRegistry;

    /**
     * Default constructor.
     * @param clientRegistry The client registry
     */
    public Rx3WebSocketClientFactory(WebSocketClientRegistry<?> clientRegistry) {
        this.clientRegistry = clientRegistry;
    }

    /**
     * Injects a {@link Rx3WebSocketClient} client at the given injection point.
     * @param injectionPoint The injection point
     * @param loadBalancer   The load balancer to use (Optional)
     * @param configuration  The configuration (Optional)
     * @param beanContext    The bean context to use
     * @return The Streaming HTTP Client
     */
    @Bean
    @Secondary
    protected Rx3WebSocketClient webSocketClient(@Nullable InjectionPoint<?> injectionPoint,
                                                 @Parameter @Nullable LoadBalancer loadBalancer,
                                                 @Parameter @Nullable HttpClientConfiguration configuration,
                                                 BeanContext beanContext) {
        return new BridgedRx3WebSocketClient(clientRegistry.resolveWebSocketClient(injectionPoint, loadBalancer, configuration, beanContext));
    }
}
