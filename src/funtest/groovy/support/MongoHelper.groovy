package support

import com.mongodb.BasicDBObject
import com.mongodb.MongoClient
import com.mongodb.WriteConcern
import com.mongodb.client.MongoDatabase
import org.bson.types.ObjectId

import java.util.concurrent.atomic.AtomicLong

import static com.mongodb.client.model.Filters.and
import static com.mongodb.client.model.Filters.eq

class MongoHelper {

    static id = new AtomicLong()

    static prepareDB(){
        def mongo = new MongoClient()
        mongo.writeConcern = WriteConcern.UNACKNOWLEDGED
        mongo.getDatabase("sdkman")
    }

    static insertAliveInDb(MongoDatabase db){
        def collection  = db.getCollection("application", BasicDBObject)
        def basicDbObject = new BasicDBObject()
        basicDbObject.append("_id", id.getAndIncrement().toString())
        basicDbObject.append("alive", "OK")
        collection.insertOne(basicDbObject)
    }

    //{
    //  "_id" : ObjectId("5756891ce714930013b96df2"),
    //  "_class" : "Version",
    //  "candidate" : "groovy",
    //  "version" : "2.4.7",
    //  "url" : "http://dl.bintray.com/groovy/maven/apache-groovy-binary-2.4.7.zip"
    // }
    static insertCandidateVersionInDb(MongoDatabase db, String candidate, String version, String target) {
        def collection  = db.getCollection("versions", BasicDBObject)
        def basicDbObject = new BasicDBObject()
        basicDbObject.append("_id", ObjectId.get())
        basicDbObject.append("_class", "Version")
        basicDbObject.append("candidate", candidate)
        basicDbObject.append("version", version)
        basicDbObject.append("url", target)
        collection.insertOne(basicDbObject)
    }

    static readAuditEntry(MongoDatabase db, String candidate, String version) {
        def collection = db.getCollection("audit", BasicDBObject)
        Optional.ofNullable(collection.find(and(eq("candidate", candidate), eq("version", version))).first()?.getString("version"))
    }


    static clean(MongoDatabase db) {
        db.drop()
    }
}
