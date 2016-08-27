package steps

import wslite.rest.RESTClientException

import static cucumber.api.groovy.EN.And
import static support.MongoHelper.clean
import static support.MongoHelper.insertAliveInDb

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

And(~/^the response contains "([^"]*)" as "([^"]*)"$/) { String key, String value ->
    assert response.json[(key)] == value
}

And(~/^the response contains an "([^"]*)"$/) { String key ->
    assert response.json[(key)]
}