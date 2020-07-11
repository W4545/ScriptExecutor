package parts.lost.mc.scriptexecutor.kotlin.commands.commandscriptexecute

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object Help: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        sender.sendMessage("Help subcommand is not implemented at this time")

        return true
    }
}