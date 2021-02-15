package parts.lost.mc.scriptexecutor.kotlin.commands.commandseautomation

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import parts.lost.mc.scriptexecutor.kotlin.constructs.BasicHelpNotes
import parts.lost.mc.scriptexecutor.kotlin.interfaces.SubCommand

object CommandList : SubCommand {
    override val name = "list"
    override val helpNotes = BasicHelpNotes(
        this,
        "",
        "Lists the currently automated scripts"
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        TODO("Not yet implemented")
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        TODO("Not yet implemented")
    }
}