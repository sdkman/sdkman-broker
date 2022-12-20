package io.sdkman.broker.binary

import io.sdkman.broker.app.AppRepo
import ratpack.handling.Context
import ratpack.handling.Handler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BinaryVersionHandler @Inject constructor(private val appRepo: AppRepo) : Handler {

    override fun handle(ctx: Context) {
        when (val channel = ctx.pathTokens["channel"]) {
            null -> ctx.clientError(400)
            channel -> {
                val impl = ctx.pathTokens["impl"] ?: "bash"
                appRepo.findVersion(impl, channel).then(ctx::render)
            }
        }
    }
}