package dev.jacaro.mc.scriptexecutor.kotlin.commands.commandseautomation

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.BasicHelpNotes
import dev.jacaro.mc.scriptexecutor.kotlin.emptyMutableList
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.HelpNotes
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.SubCommand
import dev.jacaro.mc.scriptexecutor.kotlin.storage.Storage

object DebugAutomation : SubCommand {
    override val name: String
        get() = "debug"
    override val helpNotes: HelpNotes
        get() = BasicHelpNotes(
            this,
            "debug <automated script ID>",
            "Displays the configuration of the provided automated script."
        )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size != 1)
            sender.sendMessage("${ChatColor.RED}Incorrect number of arguments provided.")
        else {
            Storage.automatedScripts.find { it.scriptID == args[0] }?.scriptConfiguration?.let {
                sender.sendMessage(it.verbose)
            } ?: sender.sendMessage("${ChatColor.RED}Error: invalid Script ID.")
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String>  = when(args.size) {
        2 -> Storage.automatedScripts.map { it.scriptID }.toMutableList()
        else -> emptyMutableList()
    }
}