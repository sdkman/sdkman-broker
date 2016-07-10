package steps

import wslite.rest.RESTClient

import static cucumber.api.groovy.EN.And

httpClient = new RESTClient("http://localhost:5050")
httpClient.defaultContentTypeHeader = "application/json"
httpClient.defaultCharset = "UTF-8"

And(~/^a running service$/) { ->
}

And(~/^the service is queried on "([^"]*)"$/) { String arg1 ->
    response = httpClient.get(path: "/alive")
}

And(~/^the service response status is (\d+)$/) { int status ->
    assert response.statusCode == status
}

And(~/^the service response body is "([^"]*)"$/) { String message ->
    assert response.json.message == message
}