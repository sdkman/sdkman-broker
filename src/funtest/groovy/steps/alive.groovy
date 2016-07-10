package steps

import org.spockframework.runtime.SpockAssertionError
import spock.util.concurrent.PollingConditions
import wslite.rest.RESTClient
import wslite.rest.RESTClientException

import static cucumber.api.groovy.EN.And

httpClient = new RESTClient("http://localhost:5050")
httpClient.defaultContentTypeHeader = "application/json"
httpClient.defaultCharset = "UTF-8"

def pollingConditions = new PollingConditions(timeout: 5, delay: 0.5)

And(~/^a running service$/) { ->
}

And(~/^the service is queried on "([^"]*)"$/) { String arg1 ->
    pollingConditions.eventually {
        try {
            response = httpClient.get(path: "/alive")
        } catch (RESTClientException rce) {
            throw new SpockAssertionError(rce.message)
        }
    }
}

And(~/^the service response status is (\d+)$/) { int status ->
    assert response.statusCode == status
}

And(~/^the service response body is "([^"]*)"$/) { String message ->
    assert response.json.message == message
}