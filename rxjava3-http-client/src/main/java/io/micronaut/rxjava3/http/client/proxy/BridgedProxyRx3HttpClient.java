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
package io.micronaut.rxjava3.http.client.proxy;

import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.client.ProxyHttpClient;
import io.reactivex.rxjava3.core.Flowable;


/**
 * Internal bridge for the {@link ProxyHttpClient} client.
 *
 * @author James Kleeh
 * @since 2.1.0
 */
@Internal
class BridgedProxyRx3HttpClient implements Rx3ProxyHttpClient {

    private final ProxyHttpClient proxyHttpClient;

    BridgedProxyRx3HttpClient(ProxyHttpClient proxyHttpClient) {
        this.proxyHttpClient = proxyHttpClient;
    }

    @Override
    public Flowable<MutableHttpResponse<?>> proxy(@NonNull HttpRequest<?> request) {
        return Flowable.fromPublisher(proxyHttpClient.proxy(request));
    }
}
