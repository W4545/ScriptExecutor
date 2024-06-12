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
package dev.jacaro.mc.scriptexecutor.kotlin.storage

import java.util.*

object Storage {
    val runningScripts = mutableSetOf<RunningScript>()

    val automatedScripts = mutableSetOf<AutomatedScript>()

    private val scriptCount = mutableMapOf<String, Int>()

    fun scriptName(name: String): String {
        val count = scriptCount[name] ?: 0
        scriptCount[name] = count + 1
        return "$name-$count"
    }

    fun automatedScriptID(name: String): String {
        val calendar = Calendar.getInstance()
        return "$name-${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.HOUR_OF_DAY)}-${calendar.get(Calendar.MINUTE)}-${calendar.get(Calendar.MILLISECOND)}"
    }
}