package steps

import wslite.rest.RESTClient
import wslite.rest.RESTClientException

import static cucumber.api.groovy.EN.And
import static support.MongoHelper.*

httpClient = new RESTClient("http://localhost:5050")
httpClient.defaultContentTypeHeader = "application/json"
httpClient.defaultCharset = "UTF-8"

And(~/^an initialised database$/) { ->
    insertAliveInDb(db)
}

And(~/^an uninitialised database$/) { ->
    clean(db)
}

And(~/^an inaccessible database$/) { ->
    //can't implement
}

And(~/^the service is queried on "([^"]*)"$/) { String path ->
    try {
        response = httpClient.get(path: path)
    } catch (RESTClientException rce) {
        response = rce.response
    }
}

And(~/^the service response status is (\d+)$/) { int status ->
    assert response.statusCode == status
}