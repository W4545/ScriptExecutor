/**
 * Copyright (C) 2025 Jack Young
 * This file is part of ScriptExecutor <https://github.com/W4545/ScriptExecutor>.
 *
 * ScriptExecutor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ScriptExecutor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ScriptExecutor.  If not, see <http://www.gnu.org/licenses/>.
 */
package dev.jacaro.mc.scriptexecutor.kotlin.automation

import org.bukkit.scheduler.BukkitTask
import dev.jacaro.mc.scriptexecutor.kotlin.ScriptExecutor
import dev.jacaro.mc.scriptexecutor.kotlin.automation.config.AutomationConfigManager
import dev.jacaro.mc.scriptexecutor.kotlin.config.ScriptConfiguration
import dev.jacaro.mc.scriptexecutor.kotlin.exceptions.ScriptCancelException
import dev.jacaro.mc.scriptexecutor.kotlin.scriptrunner.CreateScript
import dev.jacaro.mc.scriptexecutor.kotlin.storage.AutomatedScript
import dev.jacaro.mc.scriptexecutor.kotlin.storage.Storage
import java.time.Instant
import java.util.*
import java.util.logging.Level

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
        if (period <= 0L)
            ScriptExecutor.plugin.server.scheduler.runTaskLater(ScriptExecutor.plugin, it, delay)
        else
            ScriptExecutor.plugin.server.scheduler.runTaskTimer(ScriptExecutor.plugin, it, delay, period)
    }

    fun schedule(scriptConfiguration: ScriptConfiguration, delay: Long) : AutomatedScript = schedule(scriptConfiguration, true) {
        ScriptExecutor.plugin.server.scheduler.runTaskLater(ScriptExecutor.plugin, it, delay)
    }

    private fun schedule(scriptConfiguration: ScriptConfiguration, deleteOnCompletion: Boolean, initializer: (Runnable) -> BukkitTask) : AutomatedScript {
        val scriptID = if (scriptConfiguration.additionalConfigurations["automatedScriptID"] !is String) {
            val id = Storage.automatedScriptID(scriptConfiguration.name)
            scriptConfiguration.additionalConfigurations["automatedScriptID"] = id
            id
        } else {
            scriptConfiguration.additionalConfigurations["configGenerated"] = true
            scriptConfiguration.additionalConfigurations["automatedScriptID"] as String
        }

        scriptConfiguration.additionalConfigurations["automated"] = "true"

        scriptConfiguration.additionalConfigurations["automateDelete"] = deleteOnCompletion.toString()

        val automatedScript = AutomatedScript(scriptID, scriptConfiguration, null, deleteOnCompletion)
        Storage.automatedScripts.add(automatedScript)

        val bukkitTask = initializer {
            scriptConfiguration.additionalConfigurations["Last Run"] = Date.from(Instant.now())
            CreateScript.create(scriptConfiguration)
            if (automatedScript.deleteOnCompletion)
                cancel(automatedScript, false)
        }

        automatedScript.bukkitTask = bukkitTask
        AutomationConfigManager.writeScript(automatedScript)

        return automatedScript
    }

    fun cancel(automatedScript: AutomatedScript, cancelTask: Boolean = true) {
        if (cancelTask)
            automatedScript.bukkitTask?.cancel() ?: throw ScriptCancelException()
        ScriptExecutor.plugin.logger.log(Level.INFO,
            "Removing script \"${automatedScript.scriptID}\" from storage: " +
                    "${Storage.automatedScripts.removeIf { it.scriptID == automatedScript.scriptID }}")
        AutomationConfigManager.deleteScript(automatedScript.scriptID)
    }
}