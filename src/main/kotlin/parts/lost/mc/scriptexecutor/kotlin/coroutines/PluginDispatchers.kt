package parts.lost.mc.scriptexecutor.kotlin.coroutines

import kotlinx.coroutines.Dispatchers
import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
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