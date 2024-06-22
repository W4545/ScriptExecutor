/**
 * Copyright (C) 2024 Jack Young
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
package dev.jacaro.mc.scriptexecutor.kotlin.constructs

import dev.jacaro.mc.scriptexecutor.kotlin.config.ScriptConfiguration
import dev.jacaro.mc.scriptexecutor.kotlin.config.versions.ConfigVersion
import dev.jacaro.mc.scriptexecutor.kotlin.exceptions.ScriptInvalidPermissionsException
import dev.jacaro.mc.scriptexecutor.kotlin.storage.AutomatedScript
import dev.jacaro.mc.scriptexecutor.kotlin.storage.RunningScript
import dev.jacaro.mc.scriptexecutor.kotlin.storage.Storage
import org.bukkit.command.CommandSender

fun ConfigVersion.getScriptOrThrow(sender: CommandSender, name: String, configuration: String): ScriptConfiguration? {
    return if (name !in getScriptNames())
        null
    else if (sender.hasPermission("scriptexecutor.script.$name"))
        getScript(name, configuration)
    else
        throw ScriptInvalidPermissionsException("You do not have permission to access this script.")
}

    fun ConfigVersion.getScriptsAccessible(sender: CommandSender): List<ScriptConfiguration> = getScripts()
    .filter { script ->
        sender.hasPermission("scriptexecutor.script.${script.name}")
    }

fun ConfigVersion.getScriptNamesAccessible(sender: CommandSender) : List<String> = getScriptNames()
    .filter { script ->
        sender.hasPermission("scriptexecutor.script.$script")
    }

fun Storage.runningScriptsAccessible(sender: CommandSender) : List<RunningScript>
    = runningScripts.filter { runningScript ->
        sender.hasPermission("scriptexecutor.script.${runningScript.scriptConfiguration.name}")
}

fun Storage.getRunningScriptOrThrow(sender: CommandSender, id: String) : RunningScript? {
    return if (id !in runningScripts.map { it.id })
        null
    else if (sender.hasPermission("scriptexecutor.script.$id"))
        runningScripts.find { it.id == id }
    else
        throw ScriptInvalidPermissionsException("You do not have permission to access this script.")
}

fun Storage.automatedScriptsAccessible(sender: CommandSender) : List<AutomatedScript> = automatedScripts.filter {
    sender.hasPermission("scriptexecutor.script.${it.scriptConfiguration.name}")
}

fun Storage.getAutomatedScriptOrThrow(sender: CommandSender, id: String) : AutomatedScript? {
    return if (id !in automatedScripts.map { it.scriptID })
        null
    else {
        val script = automatedScripts.find { it.scriptID == id }

        if (script == null)
            null
        else if (sender.hasPermission("scriptexecutor.script.${script.scriptConfiguration.name}"))
            script
        else
            throw ScriptInvalidPermissionsException("You do not have permission to access this script.")
    }
}