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
package dev.jacaro.mc.scriptexecutor.kotlin.commands.commandscriptexecute

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import dev.jacaro.mc.scriptexecutor.kotlin.config.ConfigManager
import dev.jacaro.mc.scriptexecutor.kotlin.config.ScriptConfiguration
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.BasicHelpNotes
import dev.jacaro.mc.scriptexecutor.kotlin.emptyMutableList
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.SubCommand
import dev.jacaro.mc.scriptexecutor.kotlin.scriptrunner.CreateScript

object Exec: SubCommand {
    override val name = "exec"
    override val helpNotes = BasicHelpNotes(this, "${ChatColor.ITALIC}<script>${ChatColor.RESET} <optional configuration> <optional arguments..>", "Executes the provided script")

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val script: ScriptConfiguration? = when (args.size) {
            2 -> ConfigManager.getScript(args[0], args[1])
            1 -> {
                if (sender is Player && ConfigManager.getScriptSchemeConfigurations(args[0]).contains("player"))
                    ConfigManager.getScript(args[0], "player")
                else if (sender is ConsoleCommandSender && ConfigManager.getScriptSchemeConfigurations(args[0]).contains("console"))
                    ConfigManager.getScript(args[0], "console")
                else
                    ConfigManager.getScript(args[0], "default")
            }
            0 -> {
                sender.sendMessage("${ChatColor.RED}This command requires an argument")
                return true
            }
            else -> {
                ConfigManager.getScript(args[0], args[1])
            }
        }

        val commandArgs: Array<out String> = when (args.size) {
            1, 2 -> emptyArray()
            else -> args.copyOfRange(2, args.size)
        }

        if (script == null)
            sender.sendMessage("${ChatColor.RED}Unable to locate script entry \"${args[0]}\" with " +
                    "scheme \"${if(args.size == 2) args[1] else "Unknown"}\" in config")
        else {
            if (sender.hasPermission("scriptexecutor.script.${script.name}"))
                CreateScript.create(script, commandArgs, sender)
            else
                sender.sendMessage("${ChatColor.RED}You do not have permission to execute script ${script.name}.")
        }

        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return when (args.size) {
            2 -> ConfigManager.getScriptNames().filter { script ->
                sender.hasPermission("scriptexecutor.script.$script")
            }.toMutableList()
            3 -> ConfigManager.getScriptSchemeConfigurations(args[1]).toMutableList()
            else -> emptyMutableList()
        }
    }
}
