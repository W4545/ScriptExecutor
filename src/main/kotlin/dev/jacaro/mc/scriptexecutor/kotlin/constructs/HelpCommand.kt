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
package dev.jacaro.mc.scriptexecutor.kotlin.constructs

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.HelpNotes
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.SubCommand


class HelpCommand(val commandManager: CommandManager) : SubCommand, HelpNotes {
    override val name = "help"
    override val helpNotes = object : HelpNotes {
        override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>
        ): Boolean {
            sender.sendMessage("Description: Provides information on subcommands.")
            sender.sendMessage("Valid Subcommands: ${commandManager.subCommands.joinToString(", ") { it.name } }")
            sender.sendMessage("Usage: /$label help <subcommand>")

            return true
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        when {
            args.size == 1 -> commandManager.subCommands.find { it.name == args[0] }
                ?.helpNotes?.onCommand(sender, command, label, args)
                ?: sender.sendMessage("This is not a valid subcommand. Do /$label help for more info.")
            args.isEmpty() -> helpNotes.onCommand(sender, command, label, args)
            else -> sender.sendMessage("${ChatColor.RED}This command takes one optional argument. See /$label help for details.")
        }

        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>
    ): MutableList<String> {
        return if (args.size == 1 || (args.size == 2 && args[0] == "help"))
            commandManager.subCommands.map { it.name }.toMutableList()
        else
            MutableList(0) { "" }
    }

}