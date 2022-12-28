package io.sdkman.broker.app

import com.mongodb.client.model.Filters.eq
import io.sdkman.broker.db.MongoProvider
import ratpack.exec.Blocking
import ratpack.exec.Promise
import javax.inject.Inject
import javax.inject.Singleton

const val APPLICATION_COLLECTION = "application"
const val ALIVE_FIELD = "alive"
const val ALIVE_VALUE = "OK"

@Singleton
open class AppRepo @Inject constructor(private val mongoProvider: MongoProvider) {

    fun healthCheck(): Promise<String?> =
        Blocking.get {
            mongoProvider.database()
                .getCollection(APPLICATION_COLLECTION)
                .find(eq(ALIVE_FIELD, ALIVE_VALUE))
                .first()
                .getString(ALIVE_FIELD)
        }

    fun findVersion(impl: String, channel: String): Promise<String> =
        Blocking.get {
            mongoProvider
                .database()
                .getCollection(APPLICATION_COLLECTION)
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