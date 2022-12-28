package io.sdkman.broker.health

import io.sdkman.broker.app.AppRepo
import ratpack.exec.Promise
import ratpack.health.HealthCheck
import ratpack.health.HealthCheck.Result
import ratpack.registry.Registry
import javax.inject.Inject
import javax.inject.Singleton

const val UNHEALTHY_MESSAGE = "Nothing found at application/alive in database."

@Singleton
class MongoHealthCheck @Inject constructor(private val appRepo: AppRepo) : HealthCheck {

    override fun getName(): String = "alive"

    override fun check(registry: Registry): Promise<Result> =
        appRepo.healthCheck().map { os ->
            when (os) {
                null -> Result.unhealthy(UNHEALTHY_MESSAGE)
                else -> Result.healthy()
            }
        }
}