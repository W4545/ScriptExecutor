package dev.jacaro.mc.scriptexecutor.kotlin.constructs

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.HelpNotes
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.SubCommand


class BasicHelpNotes(val subCommand: SubCommand, val usage: String, val description: String? = null) : HelpNotes {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (description != null)
            sender.sendMessage("Description: $description")
        sender.sendMessage("Usage: /$label ${subCommand.name} $usage")

        return true
    }
}