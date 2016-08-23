package steps

import wslite.rest.RESTClientException

import static cucumber.api.groovy.EN.*
import static support.MongoHelper.clean
import static support.MongoHelper.insertCandidateVersionInDb

Given(~/^a valid Candidate Version "(.*)" "(.*)" hosted at "(.*)"$/) { String candidate, String version, url ->
    insertCandidateVersionInDb(db, candidate, version, url)
}

When(~/^I request the download URI "(.*)"$/) { path ->
    try {
        response = httpClient.get(path: path, followRedirects: false)
    } catch (RESTClientException rce) {
        response = rce.response
    }
}

Then(~/^a "(.*)" status redirecting to "(.*)" is issued$/) { String status, String url ->
    assert response.statusCode == statuses[(status)]
    assert response.headers['Location'] == url
}

Then(~/^a "(.*)" status is issued$/) { String status ->
    assert response.statusCode == statuses[(status)]
}

Given(~/^a Candidate "(.*)" does not exist$/) { String candidate ->
    clean(db)
}