package parts.lost.mc.scriptexecutor.kotlin.commands


import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.*

class CommandScriptExecute : SECommand("scriptexecute") {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        when {
            args.size == 1 -> {

                sender.sendMessage("Script execution started.")

            }
            args.isEmpty() -> {
                sender.sendMessage("${ChatColor.RED}This command requires an argument")
            }
            else -> {
                sender.sendMessage("${ChatColor.RED}This command accepts only one argument")
            }
        }

        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {

        return if (args.size == 1)
            MutableList(2) { "Hello$it" }
        else
            Collections.emptyList()
    }

}