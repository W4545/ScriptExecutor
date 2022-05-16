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
package dev.jacaro.mc.scriptexecutor.kotlin.coroutines

import kotlinx.coroutines.Dispatchers
import dev.jacaro.mc.scriptexecutor.kotlin.ScriptExecutor
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

object PluginDispatchers {

    val async: CoroutineContext by lazy {
        AsyncSpigotDispatcher(ScriptExecutor.plugin)
    }

    val sync: CoroutineContext by lazy {
        SyncSpigotDispatcher(ScriptExecutor.plugin)
    }
}


val Dispatchers.async: CoroutineContext
    get() = PluginDispatchers.async

val Dispatchers.sync: CoroutineContext
    get() = PluginDispatchers.sync


val ioCoroutineScope = CoroutineScope(Dispatchers.IO)

val synchronousCoroutineScope = CoroutineScope(Dispatchers.sync)

val asyncCoroutineScope = CoroutineScope(Dispatchers.async)