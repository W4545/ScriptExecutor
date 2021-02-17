package parts.lost.mc.scriptexecutor.kotlin.commands.commandseautomation

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import parts.lost.mc.scriptexecutor.kotlin.constructs.BasicHelpNotes
import parts.lost.mc.scriptexecutor.kotlin.interfaces.SubCommand
import parts.lost.mc.scriptexecutor.kotlin.storage.Storage

object AutomatedScriptList : SubCommand {
    override val name = "list"
    override val helpNotes = BasicHelpNotes(
        this,
        "",
        "Lists the currently automated scripts"
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isNotEmpty())
            sender.sendMessage("${ChatColor.RED}This command does not take any arguments.")
        else {
            if (Storage.automatedScripts.size == 0)
                sender.sendMessage("There are currently no automated scripts")
            else
                sender.sendMessage("Automated scripts: ${Storage.automatedScripts.joinToString { it.scriptID }}")
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ) : MutableList<String> = MutableList(0) { "" }
}