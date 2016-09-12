package io.sdkman.broker.audit;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.sdkman.broker.db.MongoProvider;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class AuditRepo {

    private final static Logger LOG = LoggerFactory.getLogger(AuditRepo.class);

    private MongoProvider mongoProvider;

    @Inject
    public AuditRepo(MongoProvider mongoProvider) {
        this.mongoProvider = mongoProvider;
    }

    public void record(AuditEntry auditEntry) throws Exception {
        MongoDatabase database = mongoProvider.database();
        MongoCollection<BasicDBObject> collection = database.getCollection("audit", BasicDBObject.class);
        BasicDBObject basicDbObject = new BasicDBObject();
        basicDbObject.append("_id", ObjectId.get());
        basicDbObject.append("command", auditEntry.getCommand());
        basicDbObject.append("candidate", auditEntry.getCandidate());
        basicDbObject.append("version", auditEntry.getVersion());
        basicDbObject.append("host", auditEntry.getHost());
        basicDbObject.append("agent", auditEntry.getAgent());
        basicDbObject.append("platform", auditEntry.getPlatform());
        basicDbObject.append("timestamp", auditEntry.getTimestamp());
        collection.insertOne(basicDbObject);
        LOG.debug("Logged: " + auditEntry);
    }
}
