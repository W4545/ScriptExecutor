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
package dev.jacaro.mc.scriptexecutor.kotlin.commands.commandseautomation

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.BasicHelpNotes
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.automatedScriptsAccessible
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.getAutomatedScriptOrThrow
import dev.jacaro.mc.scriptexecutor.kotlin.emptyMutableList
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.HelpNotes
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.SubCommand
import dev.jacaro.mc.scriptexecutor.kotlin.storage.Storage

object DebugAutomation : SubCommand {
    override val name: String
        get() = "debug"
    override val helpNotes: HelpNotes
        get() = BasicHelpNotes(
            this,
            "debug <automated script ID>",
            "Displays the configuration of the provided automated script."
        )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size != 1)
            sender.sendMessage("${ChatColor.RED}Incorrect number of arguments provided.")
        else {
            Storage.getAutomatedScriptOrThrow(sender, args[0])?.scriptConfiguration?.let {
                sender.sendMessage(it.verbose)
            } ?: sender.sendMessage("${ChatColor.RED}Error: invalid Script ID.")
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String>  = when(args.size) {
        2 -> Storage.automatedScriptsAccessible(sender).map { it.scriptID }.toMutableList()
        else -> emptyMutableList()
    }
}