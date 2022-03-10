package io.micronaut.rxjava3.http.client

import spock.lang.Specification
import spock.util.concurrent.PollingConditions

class RxClientCloseSpec extends Specification {
    void "confirm RxHttpClient can be stopped"() {
        given:
        Rx3HttpClient client = Rx3HttpClient.create(new URL("http://localhost"))

        expect:
        client.isRunning()

        when:
        client.stop()
        then:
        new PollingConditions().eventually {
            !client.isRunning()
        }
    }

    void "confirm RxHttpClient can be closed"() {
        given:
        Rx3HttpClient client = Rx3HttpClient.create(new URL("http://localhost"))

        expect:
        client.isRunning()

        when:
        client.close()
        
        then:
        new PollingConditions().eventually {
            !client.isRunning()
        }
    }
}
