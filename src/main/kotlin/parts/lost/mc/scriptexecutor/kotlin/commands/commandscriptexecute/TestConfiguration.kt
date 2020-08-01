package parts.lost.mc.scriptexecutor.kotlin.commands.commandscriptexecute

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import parts.lost.mc.scriptexecutor.kotlin.commands.CommandScriptExecute
import parts.lost.mc.scriptexecutor.kotlin.config.ConfigManager

object TestConfiguration: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        val script = when (args.size) {
            2 -> {
                ConfigManager.getScript(args[0], args[1])
            }
            1 -> {
                ConfigManager.getScript(args[0], CommandScriptExecute.infer(sender, args))
            }
            else -> {
                sender.sendMessage("${ChatColor.RED}This command takes one or two arguments." +
                        "See /$label help testconfiguration")
                return true
            }
        }
        sender.sendMessage(script?.verbose ?: "${ChatColor.RED} Unable to find or infer configuration.")

        return true
    }
}