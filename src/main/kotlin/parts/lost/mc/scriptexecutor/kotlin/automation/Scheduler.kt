package parts.lost.mc.scriptexecutor.kotlin.automation

import org.bukkit.scheduler.BukkitTask
import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
import parts.lost.mc.scriptexecutor.kotlin.config.ScriptConfiguration
import parts.lost.mc.scriptexecutor.kotlin.scriptrunner.CreateScript
import parts.lost.mc.scriptexecutor.kotlin.storage.AutomatedScript
import parts.lost.mc.scriptexecutor.kotlin.storage.Storage
import java.util.*

object Scheduler {

    fun schedule(scriptConfiguration: ScriptConfiguration, date: Date) : AutomatedScript {
        val time: Long = System.currentTimeMillis() - date.time

        return if (time < 0)
            schedule(scriptConfiguration, 0L)
        else
            schedule(scriptConfiguration, time / 1000 * 20)
    }

    fun schedule(scriptConfiguration: ScriptConfiguration, delay: Long, period: Long)
    : AutomatedScript = schedule(scriptConfiguration) {
        ScriptExecutor.plugin.server.scheduler.runTaskTimer(ScriptExecutor.plugin, it, delay, period)
    }

    fun schedule(scriptConfiguration: ScriptConfiguration, delay: Long) : AutomatedScript = schedule(scriptConfiguration) {
        ScriptExecutor.plugin.server.scheduler.runTaskLater(ScriptExecutor.plugin, it, delay)
    }

    private fun schedule(scriptConfiguration: ScriptConfiguration, initializer: (Runnable) -> BukkitTask) : AutomatedScript {
        val runnable: () -> Unit = {
            CreateScript.create(scriptConfiguration)
        }

        val bukkitTask = initializer(runnable)

        val automatedScript = AutomatedScript(Storage.automatedScriptID(scriptConfiguration.name), scriptConfiguration, bukkitTask)

        Storage.automatedScripts.add(automatedScript)

        return automatedScript
    }

    fun cancel(automatedScript: AutomatedScript) {
        automatedScript.bukkitTask.cancel()
    }
}