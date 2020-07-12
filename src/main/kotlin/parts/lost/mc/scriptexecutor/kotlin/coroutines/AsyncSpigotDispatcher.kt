package parts.lost.mc.scriptexecutor.kotlin.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import kotlin.coroutines.CoroutineContext

class AsyncSpigotDispatcher(private val plugin: Plugin): CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (Bukkit.isPrimaryThread())
            block.run()
        else
            plugin.server.scheduler.runTaskAsynchronously(plugin, block)
    }
}

