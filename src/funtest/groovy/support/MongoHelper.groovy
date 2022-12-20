package support

import com.mongodb.BasicDBObject
import com.mongodb.MongoClient
import com.mongodb.WriteConcern
import com.mongodb.client.MongoDatabase
import org.bson.types.ObjectId
import scala.Option

import java.util.concurrent.atomic.AtomicLong

import static com.mongodb.client.model.Filters.and
import static com.mongodb.client.model.Filters.eq

class MongoHelper {

    static id = new AtomicLong()

    static prepareDB() {
        def mongo = new MongoClient()
        mongo.writeConcern = WriteConcern.UNACKNOWLEDGED
        mongo.getDatabase("sdkman")
    }

    static insertAliveInDb(MongoDatabase db) {
        def collection = db.getCollection("application", BasicDBObject)
        def basicDbObject = new BasicDBObject()
        basicDbObject.append("_id", id.getAndIncrement().toString())
        basicDbObject.append("alive", "OK")
        collection.insertOne(basicDbObject)
    }

    private static insertApplicationField(MongoDatabase db, String field, String value) {
        def collection = db.getCollection("application", BasicDBObject)
        def basicDbObject = new BasicDBObject()
        basicDbObject.append("_id", id.getAndIncrement().toString())
        basicDbObject.append(field, value)
        collection.insertOne(basicDbObject)
    }

    static insertStableCliVersionInDb(MongoDatabase db, String version) {
        insertApplicationField(db, "stableCliVersion", version)
    }

    static insertStableNativeVersionInDb(MongoDatabase db, String version) {
        insertApplicationField(db, "stableNativeCliVersion", version)
    }

    static insertBetaCliVersionInDb(MongoDatabase db, String version) {
        insertApplicationField(db, "betaCliVersion", version)
    }

    static insertBetaNativeCliVersionInDb(MongoDatabase db, String version) {
        insertApplicationField(db, "betaNativeCliVersion", version)
    }

    //{
    //  "_id" : ObjectId("5756891ce714930013b96df2"),
    //  "_class" : "Version",
    //  "candidate" : "groovy",
    //  "version" : "2.4.7",
    //  "platform" : "UNIVERSAL"
    //  "url" : "http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip"
    // }
    static insertCandidateVersionInDb(MongoDatabase db, String candidate, String version, String platform, String target) {
        def collection = db.getCollection("versions", BasicDBObject)
        def basicDbObject = new BasicDBObject()
        basicDbObject.append("_id", ObjectId.get())
                .append("_class", "Version")
                .append("candidate", candidate)
                .append("version", version)
                .append("platform", platform)
                .append("url", target)
        collection.insertOne(basicDbObject)
    }

    static addChecksumToVersionInDb(MongoDatabase db, String candidate, String version,
                                    String platform, String algorithm, String checksum) {
        def collection = db.getCollection("versions", BasicDBObject)
        def query = new BasicDBObject()
                .append("candidate", candidate)
                .append("version", version)
                .append("platform", platform)

        collection.find(query).collect { existingDocument ->
            Option.apply(existingDocument.get("checksums")).map { checksums ->
                checksums.put algorithm, checksum
            }.orElse {
                existingDocument.put("checksums", [(algorithm): checksum])
            }
            collection.findOneAndReplace(query, existingDocument)
        }
    }

    static readAuditEntry(MongoDatabase db, String candidate, String version, String distribution, String platform) {
        def collection = db.getCollection("audit", BasicDBObject)
        Optional.ofNullable(
                collection.find(and(
                        eq("candidate", candidate),
                        eq("version", version),
                        eq("platform", platform),
                        eq("dist", distribution)))
                        .first()?.getString("version"))
    }


    static clean(MongoDatabase db) {
        db.drop()
    }
}
