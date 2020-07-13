package parts.lost.mc.scriptexecutor.kotlin.commands.commandscriptexecute

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import parts.lost.mc.scriptexecutor.kotlin.storage.Storage

object Running: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when {
            args.isEmpty() -> {
                if (Storage.runningScripts.size == 0)
                    sender.sendMessage("There are no scripts running right now.")
                else
                    sender.sendMessage("Running scripts: ${Storage.runningScripts.joinToString(" ") { it.id }}")
            }
            args.size == 1 -> {
                val runningScript = Storage.runningScripts.find { it.id == args[0] }
                if (runningScript == null)
                    sender.sendMessage("${ChatColor.RED}There is not a script with an ID \"${args[0]}\" that is running")
                else
                    sender.sendMessage("The script with the id \"${runningScript.id}\" is currently running.")
            }
            else -> {
                sender.sendMessage("${ChatColor.RED}This subcommand takes only one optional argument.")
            }
        }

        return true
    }
}