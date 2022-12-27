package io.sdkman.broker.app

import com.mongodb.client.model.Filters.eq
import io.sdkman.broker.db.MongoProvider
import ratpack.exec.Blocking
import ratpack.exec.Promise
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class AppRepo @Inject constructor(private val mongoProvider: MongoProvider) {

    private val applicationCollectionName = "application"
    private val aliveFieldName = "alive"

    fun healthCheck(): Promise<String?> =
        Blocking.get {
            mongoProvider.database()
                .getCollection(applicationCollectionName)
                .find(eq(aliveFieldName, "OK"))
                .first()
                .getString(aliveFieldName)
        }

    fun findVersion(impl: String, channel: String): Promise<String> =
        Blocking.get {
            mongoProvider
                .database()
                .getCollection(applicationCollectionName)
                .find()
                .first()
                .getString(cliVersionField(channel, impl))
        }

    private fun cliVersionField(channel: String, impl: String): String =
        when (channel) {
            "stable" ->
                when (impl) {
                    "native" -> "stableNativeCliVersion"
                    else -> "stableCliVersion"
                }

            "beta" ->
                when (impl) {
                    "native" -> "betaNativeCliVersion"
                    else -> "betaCliVersion"
                }

            else -> ""
        }

}