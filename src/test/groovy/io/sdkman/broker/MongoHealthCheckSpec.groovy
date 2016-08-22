package io.sdkman.broker

import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.bson.conversions.Bson
import ratpack.test.exec.ExecHarness
import spock.lang.AutoCleanup
import spock.lang.Specification

import static io.sdkman.broker.MongoHealthCheck.*
import static ratpack.health.HealthCheck.Result

class MongoHealthCheckSpec extends Specification {

    @AutoCleanup
    ExecHarness execHarness = ExecHarness.harness()

    def mongo = Mock(MongoDatabase)

    def mongoProvider = Mock(MongoProvider)

    def mongoHealthCheck = new MongoHealthCheck(mongoProvider)

    void "should be healthy for live database and well formed populated schema"() {
        given:
        def document = Mock(Document)
        document.getString(FIELD_NAME) >> FIELD_VALUE

        def documents = Mock(FindIterable)
        documents.first() >> document

        def collection = Mock(MongoCollection)
        mongoProvider.database() >> mongo
        mongo.getCollection(COLLECTION_NAME) >> collection
        collection.find({ it.fieldName == FIELD_NAME && it.value == FIELD_VALUE }) >> documents

        when:
        mongoHealthCheck.check(null)
        def result = execHarness.yieldSingle {
            mongoHealthCheck.check(null)
        }.value

        then:
        result.healthy
        !result.message
    }

    void "should be unhealthy for live database and no or malformed schema"() {
        given:
        def documents = Mock(FindIterable)
        documents.first() >> null

        def collection = Mock(MongoCollection)
        mongoProvider.database() >> mongo
        mongo.getCollection(COLLECTION_NAME) >> collection
        collection.find({ it.fieldName == FIELD_NAME && it.value == FIELD_VALUE }) >> documents

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

    void "should be unhealthy for live database with schema and missing data"() {
        given:
        def document = Mock(Document)
        def incorrectValue = "KO"
        document.getString(FIELD_NAME) >> incorrectValue

        def documents = Mock(FindIterable)
        documents.first() >> document

        def collection = Mock(MongoCollection)
        mongoProvider.database() >> mongo
        mongo.getCollection(COLLECTION_NAME) >> collection
        collection.find({ it.fieldName == FIELD_NAME && it.value == FIELD_VALUE }) >> documents

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
