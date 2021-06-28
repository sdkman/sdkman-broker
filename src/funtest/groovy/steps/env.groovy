package steps

import io.sdkman.broker.Main
import ratpack.test.MainClassApplicationUnderTest
import wslite.rest.RESTClient

import static support.MongoHelper.clean
import static support.MongoHelper.prepareDB
import static io.cucumber.groovy.Hooks.*

if(!binding.hasVariable("db")) {
    db = prepareDB()
    clean(db)
}

Before() {
    clean(db)
    embeddedApp = new MainClassApplicationUnderTest(Main.class)
    httpClient = new RESTClient("${embeddedApp.address}")
}

After() {
    embeddedApp.close()
}