package parts.lost.mc.scriptexecutor.kotlin.automation

import org.bukkit.scheduler.BukkitTask
import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
import parts.lost.mc.scriptexecutor.kotlin.config.ScriptConfiguration
import parts.lost.mc.scriptexecutor.kotlin.exceptions.ScriptCancelException
import parts.lost.mc.scriptexecutor.kotlin.scriptrunner.CreateScript
import parts.lost.mc.scriptexecutor.kotlin.storage.AutomatedScript
import parts.lost.mc.scriptexecutor.kotlin.storage.Storage
import java.time.Instant
import java.util.*

object Scheduler {

    fun schedule(scriptConfiguration: ScriptConfiguration, date: Date) : AutomatedScript {
        val time: Long = date.time - Date.from(Instant.now()).time

        return if (time < 0)
            schedule(scriptConfiguration, 0L)
        else
            schedule(scriptConfiguration, time / 1000 * 20)
    }

    fun schedule(scriptConfiguration: ScriptConfiguration, date: Date, interval: Long) : AutomatedScript {
        val time: Long = date.time - Date.from(Instant.now()).time

        return if (time < 0)
            schedule(scriptConfiguration, 0L, interval)
        else
            schedule(scriptConfiguration, time / 1000 * 20, interval)
    }

    fun schedule(scriptConfiguration: ScriptConfiguration, delay: Long, period: Long)
    : AutomatedScript = schedule(scriptConfiguration, false) {
        ScriptExecutor.plugin.server.scheduler.runTaskTimer(ScriptExecutor.plugin, it, delay, period)
    }

    fun schedule(scriptConfiguration: ScriptConfiguration, delay: Long) : AutomatedScript = schedule(scriptConfiguration, true) {
        ScriptExecutor.plugin.server.scheduler.runTaskLater(ScriptExecutor.plugin, it, delay)
    }

    private fun schedule(scriptConfiguration: ScriptConfiguration, deleteOnCompletion: Boolean, initializer: (Runnable) -> BukkitTask) : AutomatedScript {
        val scriptID = Storage.automatedScriptID(scriptConfiguration.name)
        scriptConfiguration.additionalConfigurations["automated"] = "true"
        scriptConfiguration.additionalConfigurations["automatedScriptID"] = scriptID
        scriptConfiguration.additionalConfigurations["automateDelete"] = deleteOnCompletion.toString()

        val automatedScript = AutomatedScript(scriptID, scriptConfiguration, null, deleteOnCompletion)
        Storage.automatedScripts.add(automatedScript)

        val bukkitTask = initializer {
            CreateScript.create(scriptConfiguration)
        }

        automatedScript.bukkitTask = bukkitTask

        return automatedScript
    }

    fun cancel(automatedScript: AutomatedScript) {
        automatedScript.bukkitTask?.cancel() ?: throw ScriptCancelException()
        Storage.automatedScripts.remove(automatedScript)
    }
}