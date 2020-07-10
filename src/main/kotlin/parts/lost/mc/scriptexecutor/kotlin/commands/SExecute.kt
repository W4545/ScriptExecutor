package parts.lost.mc.scriptexecutor.kotlin.commands


import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class SExecute : Command("scriptexecute") {

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        return false
    }
}