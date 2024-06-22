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
import dev.jacaro.mc.scriptexecutor.kotlin.commands.CommandScriptExecute
import dev.jacaro.mc.scriptexecutor.kotlin.config.ConfigManager
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.BasicHelpNotes
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.getScriptNamesAccessible
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.getScriptOrThrow
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.runningScriptsAccessible
import dev.jacaro.mc.scriptexecutor.kotlin.emptyMutableList
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.SubCommand
import dev.jacaro.mc.scriptexecutor.kotlin.storage.Storage

object TestConfiguration: SubCommand {
    override val name = "testconfiguration"
    override val helpNotes = BasicHelpNotes(
        this,
        "<script> ${ChatColor.ITALIC}<optional configuration>${ChatColor.RESET}",
        "Generates a script configuration for the given script and optional configuration."
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        val script = when (args.size) {
            2 -> {
                ConfigManager.getScriptOrThrow(sender, args[0], args[1])
            }
            1 -> {
                ConfigManager.getScriptOrThrow(sender, args[0], CommandScriptExecute.infer(sender, args))
            }
            else -> {
                sender.sendMessage("${ChatColor.RED}This command takes one or two arguments." +
                        "See /$label help testconfiguration")
                return true
            }
        }
        if (script != null)
            sender.sendMessage(script.verbose)
        else
            sender.sendMessage("${ChatColor.RED} Unable to find or infer configuration.")

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        val scriptNames = ConfigManager.getScriptNamesAccessible(sender)
        return when (args.size) {
            2 -> scriptNames.toMutableList()
            3 -> ConfigManager.getScriptSchemeConfigurations(if (scriptNames.contains(args[1])) args[1] else "").toMutableList()
            else -> emptyMutableList()
        }
    }
}