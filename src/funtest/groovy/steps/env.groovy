package steps

import wslite.rest.RESTClient

import static support.MongoHelper.clean
import static support.MongoHelper.prepareDB
import static io.cucumber.groovy.Hooks.*

appBaseUrl = "http://localhost:5050"
httpClient = new RESTClient(appBaseUrl)

if(!binding.hasVariable("db")) {
    db = prepareDB()
    clean(db)
}

Before() {
    clean(db)
}