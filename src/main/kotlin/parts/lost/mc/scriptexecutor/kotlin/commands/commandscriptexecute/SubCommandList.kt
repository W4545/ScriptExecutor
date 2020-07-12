package parts.lost.mc.scriptexecutor.kotlin.commands.commandscriptexecute

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import parts.lost.mc.scriptexecutor.kotlin.config.ConfigManager

object SubCommandList: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (args.isNotEmpty())
            sender.sendMessage("${ChatColor.RED}This subcommand does not take any arguments.")
        else
            sender.sendMessage("Available scripts: ${ConfigManager.getScripts().joinToString(" ") { it.first }}")

        return true
    }
}