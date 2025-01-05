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
package dev.jacaro.mc.scriptexecutor.kotlin.commands.commandscriptexecute

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.BasicHelpNotes
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.getRunningScriptOrThrow
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.runningScriptsAccessible
import dev.jacaro.mc.scriptexecutor.kotlin.emptyMutableList
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.SubCommand
import dev.jacaro.mc.scriptexecutor.kotlin.storage.Storage

object Running: SubCommand {
    override val name = "running"
    override val helpNotes = BasicHelpNotes(
        this,
        "${ChatColor.ITALIC}<optional script>${ChatColor.RESET}",
    "Lists all running scripts. Optionally provides whether the given script is currently running."
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when {
            args.isEmpty() -> {
                if (Storage.runningScripts.size == 0)
                    sender.sendMessage("There are no scripts running right now.")
                else
                    sender.sendMessage("Running scripts: ${Storage.runningScriptsAccessible(sender).joinToString(" ") { it.id }}")
            }
            args.size == 1 -> {
                val runningScript = Storage.getRunningScriptOrThrow(sender, args[0])
                if (runningScript == null)
                    sender.sendMessage("${ChatColor.RED}There is not a script with an ID \"${args[0]}\" that is running")
                else
                    sender.sendMessage("The script with the id \"${runningScript.id}\" is currently running.")
            }
            else -> {
                sender.sendMessage("${ChatColor.RED}This subcommand takes only one optional argument.")
            }
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        return if (args.size == 2)
            Storage.runningScriptsAccessible(sender).map { it.id }.toMutableList()
        else
            emptyMutableList()
    }
}