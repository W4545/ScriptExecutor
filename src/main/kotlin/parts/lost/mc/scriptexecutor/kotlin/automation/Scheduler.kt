package parts.lost.mc.scriptexecutor.kotlin.automation

import org.bukkit.scheduler.BukkitTask
import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
import parts.lost.mc.scriptexecutor.kotlin.config.ScriptConfiguration
import parts.lost.mc.scriptexecutor.kotlin.scriptrunner.CreateScript
import parts.lost.mc.scriptexecutor.kotlin.storage.AutomatedScript
import parts.lost.mc.scriptexecutor.kotlin.storage.Storage
import java.util.*

object Scheduler {

    fun schedule(scriptConfiguration: ScriptConfiguration, date: Date) {
        val time: Long = System.currentTimeMillis() - date.time

        if (time < 0) {
            schedule(scriptConfiguration, 0L)
        } else {
            schedule(scriptConfiguration, time / 1000 * 20)
        }
    }

    fun schedule(scriptConfiguration: ScriptConfiguration, delay: Long) {
        schedule(scriptConfiguration) {
            ScriptExecutor.plugin.server.scheduler.runTaskLater(ScriptExecutor.plugin, it, delay)
        }
    }

    private fun schedule(scriptConfiguration: ScriptConfiguration, initializer: (() -> Unit) -> BukkitTask) {
        val runnable: () -> Unit = {
            CreateScript.create(scriptConfiguration)
        }

        val bukkitTask = initializer(runnable)

        Storage.automatedScripts.add(AutomatedScript(scriptConfiguration, bukkitTask))
    }
}