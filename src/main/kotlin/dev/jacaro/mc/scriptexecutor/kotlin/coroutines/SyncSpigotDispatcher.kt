package dev.jacaro.mc.scriptexecutor.kotlin.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import kotlin.coroutines.CoroutineContext

class SyncSpigotDispatcher(private val plugin: JavaPlugin): CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (Bukkit.isPrimaryThread())
            block.run()
        else
            plugin.server.scheduler.runTask(plugin, block)
    }
}