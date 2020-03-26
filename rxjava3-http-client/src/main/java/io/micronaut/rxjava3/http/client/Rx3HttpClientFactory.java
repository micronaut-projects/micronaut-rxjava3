package io.micronaut.rxjava3.http.client;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Secondary;
import io.micronaut.http.client.RxHttpClientRegistry;
import io.micronaut.inject.InjectionPoint;

/**
 * Factory class for creating RxJava 3 clients.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Factory
public class Rx3HttpClientFactory {

    private final RxHttpClientRegistry clientRegistry;

    /**
     * Default constructor.
     * @param clientRegistry The client registry
     */
    public Rx3HttpClientFactory(RxHttpClientRegistry clientRegistry) {
        this.clientRegistry = clientRegistry;
    }

    /**
     * Injects an RxJava 3 client at the given injection point.
     * @param injectionPoint The injection point
     * @return The client
     */
    @Bean
    @Secondary
    protected BridgedRx3HttpClient httpClient(@Nullable InjectionPoint<?> injectionPoint) {
        if (injectionPoint != null) {
            return new BridgedRx3HttpClient(clientRegistry.getClient(injectionPoint.getAnnotationMetadata()));
        } else {
            return new BridgedRx3HttpClient(clientRegistry.getDefaultClient());
        }
    }
}
