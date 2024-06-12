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
package dev.jacaro.mc.scriptexecutor.kotlin.config

data class ScriptConfiguration(
        val name: String,
        val scheme: String,
        val commands: List<String>,
        val workingDirectory: String?,
        val wrapOutput: Boolean,
        val logFile: Boolean,
        val logFileLocation: String?,
        val additionalConfigurations: MutableMap<String, Any> = mutableMapOf()
) {
    val verbose: String
    get() = "Name: $name\n" +
            "  Scheme: $scheme\n" +
            "  Commands: ${commands.joinToString(" ")}\n" +
            "  Working Directory: $workingDirectory\n" +
            "  Wrap Output: $wrapOutput\n" +
            "  Generate Log File: $logFile\n" +
            "  Log File Location: $logFileLocation\n" +
            "  Additional Configurations: \n" +
            additionalConfigurations.map { "${it.key}: ${it.value}" }.joinToString("\n    ", "    ")
}