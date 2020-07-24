package parts.lost.mc.scriptexecutor.kotlin.commands.commandscriptexecute

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
import parts.lost.mc.scriptexecutor.kotlin.storage.Storage

object Reload: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isNotEmpty())
            sender.sendMessage("${ChatColor.RED}This command does not take any arguments.")
        else if (Storage.runningScripts.isNotEmpty())
            sender.sendMessage("${ChatColor.RED}No scripts can be running to reload config.")
        ScriptExecutor.plugin.reloadConfig()
        sender.sendMessage("${ChatColor.BLUE}Config reloaded.")

        return true
    }
}
