package io.sdkman.broker.audit;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.BasicDBObject;
import io.sdkman.broker.db.MongoProvider;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.exec.Blocking;

@Singleton
public class AuditRepo {

    private static final Logger logger = LoggerFactory.getLogger(AuditRepo.class);

    private final MongoProvider mongoProvider;

    @Inject
    public AuditRepo(MongoProvider mongoProvider) {
        this.mongoProvider = mongoProvider;
    }

    public void insertAudit(AuditEntry auditEntry) {
        Blocking.exec(() -> {
                    mongoProvider.database()
                            .getCollection("audit", BasicDBObject.class)
                            .insertOne(
                                    new BasicDBObject()
                                            .append("_id", ObjectId.get())
                                            .append("command", auditEntry.getCommand())
                                            .append("candidate", auditEntry.getCandidate())
                                            .append("version", auditEntry.getVersion())
                                            .append("host", auditEntry.getHost())
                                            .append("agent", auditEntry.getAgent())
                                            .append("platform", auditEntry.getPlatform())
                                            .append("dist", auditEntry.getDist())
                                            .append("timestamp", auditEntry.getTimestamp()));
                    logger.debug("Logged: {}", auditEntry);
                }
        );
    }
}
