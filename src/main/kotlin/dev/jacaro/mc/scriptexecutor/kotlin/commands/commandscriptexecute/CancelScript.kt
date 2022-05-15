package dev.jacaro.mc.scriptexecutor.kotlin.commands.commandscriptexecute

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.BasicHelpNotes
import dev.jacaro.mc.scriptexecutor.kotlin.emptyMutableList
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.HelpNotes
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.SubCommand
import dev.jacaro.mc.scriptexecutor.kotlin.storage.Storage

object CancelScript: SubCommand {
    override val name = "cancel"
    override val helpNotes: HelpNotes = BasicHelpNotes(
        this,
        "<script>",
        "Requests the provided script to be terminated. The script will attempt to shutdown properly."
    )

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

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        return if (args.size == 2)
            Storage.runningScripts.map { it.id }.toMutableList()
        else
            emptyMutableList()
    }
}