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
package dev.jacaro.mc.scriptexecutor.kotlin.constructs

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import dev.jacaro.mc.scriptexecutor.kotlin.ScriptExecutor
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.CommandInitializer
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.SubCommand


abstract class CommandManager : SubCommand, CommandInitializer {
    abstract val subCommands: List<SubCommand>

    override fun initialize() {
        val command = ScriptExecutor.plugin.getCommand(name)!!
        command.setExecutor(this)
        command.tabCompleter = this
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>)
    : MutableList<String> {
        val allowedSubCommands = subCommands.filter { subCommand ->
            sender.hasPermission("${command.permission!!}.${subCommand.name}")
        }
        return when {
            args.size == 1 -> allowedSubCommands.map { it.name }.toMutableList()
            args.size > 1 -> allowedSubCommands.find { it.name == args[0] }
                ?.onTabComplete(sender, command, alias, args) ?: MutableList(0) { "" }
            else -> MutableList(0) { "" }
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (args.isEmpty()) {
            sender.sendMessage("${ChatColor.GOLD}ScriptExecutor running version: ${ChatColor.BLUE}${ScriptExecutor.plugin.description.version}")
            sender.sendMessage("See /$label help for available subcommands.")
        } else {
            val subCommand = subCommands.find { it.name == args[0] }?.let {
                if (sender.hasPermission("${command.permission!!}.${it.permissionName}"))
                    it
                else {
                    sender.sendMessage("${ChatColor.RED}You do not have permission to execute this sub-command.")
                    return true
                }
            }

            subCommand?.onCommand(sender, command, label, args.copyOfRange(1, args.size))
                ?: sender.sendMessage("${ChatColor.RED}Please provide a valid subcommand. See /$label help for details")
        }

        return true
    }
}