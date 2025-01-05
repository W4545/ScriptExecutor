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
package dev.jacaro.mc.scriptexecutor.kotlin.config

import org.bukkit.configuration.ConfigurationSection

internal data class UnresolvedScriptConfiguration(
        val name: String,
        val scheme: String,
        var commands: List<String>? = null,
        var workingDirectory: String? = null,
        var wrapOutput: Boolean = false,
        var logFile: Boolean = false,
        var logFileLocation: String? = null
) {
    fun map(): ScriptConfiguration? {
        return commands?.let { ScriptConfiguration(name, scheme, it, workingDirectory, wrapOutput, logFile, logFileLocation) }
    }

    companion object {
        fun loadValues(configurationSection: ConfigurationSection?, unresolvedScriptConfiguration: UnresolvedScriptConfiguration) {
            configurationSection?.getStringList("commands")?.also {
                if (it.size != 0)
                    unresolvedScriptConfiguration.commands = it
            }

            configurationSection?.getString("workingDirectory")?.also {
                unresolvedScriptConfiguration.workingDirectory = it
            }

            configurationSection?.getString("wrapOutput")?.also {
                unresolvedScriptConfiguration.wrapOutput = it.toBoolean()
            }

            configurationSection?.getString("logFileLocation")?.also {
                unresolvedScriptConfiguration.logFileLocation = it
            }

            configurationSection?.getString("logFile")?.also {
                unresolvedScriptConfiguration.logFile = it.toBoolean()
            }
        }
    }
}