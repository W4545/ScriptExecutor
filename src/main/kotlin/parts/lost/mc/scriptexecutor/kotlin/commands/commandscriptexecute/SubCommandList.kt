package parts.lost.mc.scriptexecutor.kotlin.commands.commandscriptexecute

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import parts.lost.mc.scriptexecutor.kotlin.config.ConfigManager
import parts.lost.mc.scriptexecutor.kotlin.constructs.BasicHelpNotes
import parts.lost.mc.scriptexecutor.kotlin.interfaces.SubCommand

object SubCommandList: SubCommand {
    override val name = "list"
    override val helpNotes = BasicHelpNotes(
        this,
        "",
        "Lists all registered scripts"
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (args.isNotEmpty())
            sender.sendMessage("${ChatColor.RED}This subcommand does not take any arguments.")
        else
            sender.sendMessage("Available scripts: ${ConfigManager.getScriptNames().joinToString(", ")}")

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        return MutableList(0) { "" }
    }
}