package steps

import wslite.rest.RESTClientException

import static cucumber.api.groovy.EN.And

And(~/^a request is made on (.*) using (.*)$/) { String path, String platform ->
    try {
        response = httpClient.get(path: path, followRedirects: false, query: [platform: platform])
    } catch (RESTClientException rce) {
        response = rce.response
    }
}

And(~/^the service response status is (\d+)$/) { int status ->
    assert response.statusCode == status
}

And(~/^a redirect to (.*) is returned/) { String url ->
    assert response.statusCode == 302
    assert response.headers['Location'] == url
}

And(~/^the response contains (.*) as (.*)$/) { String key, String value ->
    assert response.json[(key)] == value
}

And(~/^the response contains an (.*)$/) { String key ->
    assert response.json[(key)]
}