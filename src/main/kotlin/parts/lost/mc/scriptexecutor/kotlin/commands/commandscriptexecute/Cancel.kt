package parts.lost.mc.scriptexecutor.kotlin.commands.commandscriptexecute

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import parts.lost.mc.scriptexecutor.kotlin.storage.Storage

object Cancel: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        when (args.size) {
            1 -> {
                val script = Storage.runningScripts.find { it.id == args[0] }

                if (script == null) {
                    sender.sendMessage("${ChatColor.RED}There is no script running with the provided ID.")
                } else {
                    script.process.destroy()
                    sender.sendMessage("Script termination requested.")
                }
            }
            0 -> {
                sender.sendMessage("${ChatColor.RED}This command requires an argument.")
            }
            else -> {
                sender.sendMessage("${ChatColor.RED}This command takes only one argument.")
            }
        }

        return true
    }
}