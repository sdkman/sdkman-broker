package io.sdkman.broker.download

import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import io.sdkman.broker.db.MongoProvider
import org.bson.Document
import org.bson.conversions.Bson
import ratpack.test.exec.ExecHarness
import spock.lang.AutoCleanup
import spock.lang.Specification

import static VersionRepo.COLLECTION_NAME

public class DownloadResolverSpec extends Specification {

    @AutoCleanup
    ExecHarness execHarness = ExecHarness.harness()

    def mongo = Mock(MongoDatabase)

    def mongoProvider = Mock(MongoProvider)

    def downloadResolver = new VersionRepo(mongoProvider)

    void "should retrieve an option of download url for an existing candidate version combination"() {
        given:
        def candidate = "groovy"
        def version = "2.4.7"
        def url = "http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip"

        def document = Mock(Document)
        document.getString("url") >> url

        def documents = Mock(FindIterable)
        documents.first() >> document

        def versionsCollection = Mock(MongoCollection)
        versionsCollection.find({ it instanceof Bson }) >> documents

        mongo.getCollection(COLLECTION_NAME) >> versionsCollection
        mongoProvider.database() >> mongo

        when:
        def result = execHarness.yieldSingle {
            downloadResolver.resolveDownloadUrl(candidate, version)
        }.value

        then:
        result == Optional.of("http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip")
    }

    void "should receive an option of empty for an invalid candidate version combination"() {
        given:
        def candidate = "groper"
        def version = "9.9.9"

        def documents = Mock(FindIterable)
        documents.first() >> null

        def versionsCollection = Mock(MongoCollection)
        versionsCollection.find({ it instanceof Bson }) >> documents

        mongo.getCollection(COLLECTION_NAME) >> versionsCollection
        mongoProvider.database() >> mongo


        when:
        def result = execHarness.yieldSingle {
            downloadResolver.resolveDownloadUrl(candidate, version)
        }.value

        then:
        result == Optional.empty()
    }
}
