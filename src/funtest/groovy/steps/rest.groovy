package steps

import wslite.rest.RESTClientException

import static cucumber.api.groovy.EN.And

And(~/^the service is queried on "([^"]*)"$/) { String path ->
    try {
        response = httpClient.get(path: path, followRedirects: false)
    } catch (RESTClientException rce) {
        response = rce.response
    }
}

And(~/^the service response status is (\d+)$/) { int status ->
    assert response.statusCode == status
}

And(~/^a response (.*) status redirecting to "(.*)" is returned/) { int status, String url ->
    assert response.statusCode == status
    assert response.headers['Location'] == url
}

And(~/^the response contains "([^"]*)" as "([^"]*)"$/) { String key, String value ->
    assert response.json[(key)] == value
}

And(~/^the response contains an "([^"]*)"$/) { String key ->
    assert response.json[(key)]
}