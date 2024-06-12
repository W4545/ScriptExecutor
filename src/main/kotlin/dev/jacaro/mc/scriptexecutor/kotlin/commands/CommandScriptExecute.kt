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
package dev.jacaro.mc.scriptexecutor.kotlin.commands


import org.bukkit.command.*
import org.bukkit.entity.Player
import dev.jacaro.mc.scriptexecutor.kotlin.commands.commandscriptexecute.*
import dev.jacaro.mc.scriptexecutor.kotlin.config.ConfigManager
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.CommandManager
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.HelpCommand

object CommandScriptExecute : CommandManager() {

    override val name = "scriptexecute"
    override val helpNotes = HelpCommand(this)
    override val subCommands = listOf(Exec, helpNotes, CancelScript, Reload, Running, ScriptList, TestConfiguration)


    fun infer(sender: CommandSender, args: Array<out String>): String {
        return if (sender is Player && ConfigManager.getScriptSchemeConfigurations(args[0]).contains("player"))
            "player"
        else if (sender is ConsoleCommandSender && ConfigManager.getScriptSchemeConfigurations(args[0])
                .contains("console")
        )
            "console"
        else
            "default"
    }

}
