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
package dev.jacaro.mc.scriptexecutor.kotlin.commands.commandseautomation.create

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.HelpNotes
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.SubCommand


object Create: SubCommand {

    override val name = "create"
    override val helpNotes = object : HelpNotes {
        override fun onCommand(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<out String>
        ): Boolean {
            sender.sendMessage("Description: " +
                    "Creates a new automation request with the given delay or datetime, and optional period")
            sender.sendMessage("Examples: /$label create hello console 1m 10s " +
                    "(create an automation script that runs every 10 seconds starting in 1 minute.)")
            sender.sendMessage("Examples: /$label create hello default 2021-12-1 14:30 " +
                    "(creates an automation script that runs on the date and time provided)")
            sender.sendMessage("Usage: /$label create <script> <script configuration> <delay or date> <optional time> <optional period>")

            return true
        }

    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>): Boolean
    = CreateCommandExecutor.onCommand(sender, command, label, args)

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String>
    = CreateTabCompleter.onTabComplete(sender, command, alias, args)
}