package io.sdkman.broker.health

import io.sdkman.broker.app.AppRepo
import ratpack.exec.Promise
import ratpack.test.exec.ExecHarness
import spock.lang.AutoCleanup
import spock.lang.Specification

import static io.sdkman.broker.health.MongoHealthCheck.UNHEALTHY_MESSAGE

class MongoHealthCheckSpec extends Specification {

    @AutoCleanup
    ExecHarness execHarness = ExecHarness.harness()

    def appRepo = Mock(AppRepo)

    def mongoHealthCheck = new MongoHealthCheck(appRepo)

    void "should be healthy for live database and well formed populated schema"() {
        given:
        appRepo.healthCheck() >> Promise.value(Optional.of("OK"))

        when:
        def result = execHarness.yieldSingle {
            mongoHealthCheck.check(null)
        }.value

        then:
        result.healthy
        result.message == "OK"
    }

    void "should be unhealthy for live database and no or malformed schema"() {
        given:
        appRepo.healthCheck() >> Promise.value(Optional.empty())

        when:
        mongoHealthCheck.check(null)
        def result = execHarness.yieldSingle {
            mongoHealthCheck.check(null)
        }.value

        then:
        !result.healthy
        !result.error
        result.message == UNHEALTHY_MESSAGE
    }
}
