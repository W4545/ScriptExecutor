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

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import kotlin.coroutines.CoroutineContext

class AsyncSpigotDispatcher(private val plugin: Plugin): CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (Bukkit.isPrimaryThread())
            block.run()
        else
            plugin.server.scheduler.runTaskAsynchronously(plugin, block)
    }
}

