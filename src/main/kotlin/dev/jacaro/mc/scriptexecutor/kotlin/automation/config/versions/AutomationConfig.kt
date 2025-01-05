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
package dev.jacaro.mc.scriptexecutor.kotlin.automation.config.versions

import dev.jacaro.mc.scriptexecutor.kotlin.storage.AutomatedScript


interface AutomationConfig {

    val version : Int

    val automatedScriptNames : List<String>

    fun loadScript(string: String) : AutomatedScript

    fun writeScript(automatedScript: AutomatedScript)

    fun deleteScript(string: String)
}