package support

import com.mongodb.BasicDBObject
import com.mongodb.MongoClient
import com.mongodb.WriteConcern
import com.mongodb.client.MongoDatabase

import java.util.concurrent.atomic.AtomicLong

class MongoHelper {

    static id = new AtomicLong()

    static prepareDB(){
        def mongo = new MongoClient()
        mongo.writeConcern = WriteConcern.UNACKNOWLEDGED
        mongo.getDatabase("sdkman")
    }

    static insertAliveInDb(MongoDatabase db, uid = id.getAndIncrement().toString()){
        def collection  = db.getCollection("application", BasicDBObject.class)
        def basicDbObject = new BasicDBObject()
        basicDbObject.append("_id", uid)
        basicDbObject.append("alive", "OK")
        collection.insertOne(basicDbObject)
    }

    static clean(MongoDatabase db) {
        db.drop()
    }
}
