package dev.jacaro.mc.scriptexecutor.kotlin.coroutines

import kotlinx.coroutines.Dispatchers
import dev.jacaro.mc.scriptexecutor.kotlin.ScriptExecutor
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

object PluginDispatchers {

    val async: CoroutineContext by lazy {
        AsyncSpigotDispatcher(ScriptExecutor.plugin)
    }

    val sync: CoroutineContext by lazy {
        SyncSpigotDispatcher(ScriptExecutor.plugin)
    }
}


val Dispatchers.async: CoroutineContext
    get() = PluginDispatchers.async

val Dispatchers.sync: CoroutineContext
    get() = PluginDispatchers.sync


val ioCoroutineScope = CoroutineScope(Dispatchers.IO)

val synchronousCoroutineScope = CoroutineScope(Dispatchers.sync)

val asyncCoroutineScope = CoroutineScope(Dispatchers.async)