package parts.lost.mc.scriptexecutor.kotlin.coroutines

import kotlinx.coroutines.Dispatchers
import org.bukkit.plugin.java.JavaPlugin
import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
import kotlin.coroutines.CoroutineContext

object PluginDispatchers {

    val async: CoroutineContext by lazy {
        AsyncSpigotDispatcher(JavaPlugin.getPlugin(ScriptExecutor::class.java))
    }

    val sync: CoroutineContext by lazy {
        SyncSpigotDispatcher(JavaPlugin.getPlugin(ScriptExecutor::class.java))
    }
}

val Dispatchers.async: CoroutineContext
    get() = PluginDispatchers.async

val Dispatchers.sync: CoroutineContext
    get() = PluginDispatchers.sync