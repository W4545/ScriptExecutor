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
package dev.jacaro.mc.scriptexecutor.kotlin.commands.commandseautomation

import dev.jacaro.mc.scriptexecutor.kotlin.automation.Timing
import dev.jacaro.mc.scriptexecutor.kotlin.automation.Timings
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.BasicHelpNotes
import dev.jacaro.mc.scriptexecutor.kotlin.emptyMutableList
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.SubCommand
import dev.jacaro.mc.scriptexecutor.kotlin.storage.Storage

object NextRun : SubCommand {
    override val name = "nextrun"
    override val helpNotes = BasicHelpNotes(
        this,
        "<automated script ID>",
        "Provides the next scheduled run of an automated script."
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size != 1)
            sender.sendMessage("${ChatColor.RED}This command takes one argument.")
        else {
            val automatedScript = Storage.automatedScripts.find { it.scriptID == args[0] }
            //TODO
            if (automatedScript == null) {
                sender.sendMessage("${ChatColor.RED}Invalid script ID provided.")
                return true
            } else {
                val timing  = automatedScript.scriptConfiguration.additionalConfigurations["timing"] as Timing

                if (timing.date == null) {
                    sender.sendMessage("${ChatColor.RED}Next Run command cannot currently determine the next run of a delay based script.")
                    return true
                }

                sender.sendMessage("Next Schedule run: ${Timings.dateFormatter.format(timing.date)}")
            }

        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> = when(args.size) {
        2 -> Storage.automatedScripts.map { it.scriptID }.toMutableList()
        else -> emptyMutableList()
    }
}