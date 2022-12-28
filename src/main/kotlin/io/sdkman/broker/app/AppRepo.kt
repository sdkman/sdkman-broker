package io.sdkman.broker.app

import arrow.core.Option
import arrow.core.toOption
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

    fun healthCheck(): Promise<Option<String>> =
        Blocking.get {
            mongoProvider.database()
                .getCollection(APPLICATION_COLLECTION)
                .find(eq(ALIVE_FIELD, ALIVE_VALUE))
                .first()
                .toOption()
                .map { it.getString(ALIVE_FIELD) }
        }

    fun findVersion(impl: String, channel: String): Promise<Option<String>> =
        Blocking.get {
            mongoProvider
                .database()
                .getCollection(APPLICATION_COLLECTION)
                .find()
                .first()
                .toOption()
                .map { it.getString(cliVersionField(channel, impl)) }
        }

    private fun cliVersionField(channel: String, impl: String): String =
        when (channel) {
            "stable" ->
                when (impl) {
                    "native" -> "stableNativeCliVersion"
                    else -> "stableCliVersion"
                }

            else ->
                when (impl) {
                    "native" -> "betaNativeCliVersion"
                    else -> "betaCliVersion"
                }
        }
}