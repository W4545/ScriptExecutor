/**
 * Copyright (C) 2022 Jack Young
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

import dev.jacaro.mc.scriptexecutor.kotlin.ScriptExecutor
import dev.jacaro.mc.scriptexecutor.kotlin.config.versions.ConfigVersion
import dev.jacaro.mc.scriptexecutor.kotlin.config.versions.one.ConfigVersion1
import java.lang.RuntimeException

val ConfigManager: ConfigVersion = when(ScriptExecutor.plugin.config.getInt("version")) {
    1 -> ConfigVersion1("Scripts", "Defaults")
    2 -> ConfigVersion1("scripts", "defaults", 2)
    else -> throw RuntimeException("Invalid config version")
}
