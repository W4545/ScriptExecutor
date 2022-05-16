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
package dev.jacaro.mc.scriptexecutor.kotlin.commands.commandscriptexecute

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import dev.jacaro.mc.scriptexecutor.kotlin.config.ConfigManager
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.BasicHelpNotes
import dev.jacaro.mc.scriptexecutor.kotlin.emptyMutableList
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.SubCommand

object ScriptList: SubCommand {
    override val name = "list"
    override val helpNotes = BasicHelpNotes(
        this,
        "",
        "Lists all registered scripts"
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (args.isNotEmpty())
            sender.sendMessage("${ChatColor.RED}This subcommand does not take any arguments.")
        else
            sender.sendMessage("Available scripts: ${ConfigManager.getScriptNames().joinToString(", ")}")

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> = emptyMutableList()
}