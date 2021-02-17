package parts.lost.mc.scriptexecutor.kotlin.commands.commandseautomation

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import parts.lost.mc.scriptexecutor.kotlin.constructs.BasicHelpNotes
import parts.lost.mc.scriptexecutor.kotlin.interfaces.SubCommand
import parts.lost.mc.scriptexecutor.kotlin.storage.Storage

object CancelAutomation : SubCommand {
    override val name: String = "cancel"
    override val helpNotes = BasicHelpNotes(
        this,
        "<script ID>",
        "Attempts to cancel an automation script"
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size == 1) {
            val script = Storage.automatedScripts.find { it.scriptID == args[0] }

            if (script == null) {
                sender.sendMessage("${ChatColor.RED}There is no script running with the provided ID.")
            } else {
                script.bukkitTask.cancel()
                sender.sendMessage("Script termination requested.")
            }
        } else
            sender.sendMessage("${ChatColor.RED}This command takes only one argument. " +
                    "See ${ChatColor.DARK_RED}/$label help cancel${ChatColor.RESET}${ChatColor.RED} for details.")

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        return when(args.size) {
            2 -> Storage.automatedScripts.map { it.scriptID }.toMutableList()
            else -> MutableList(0) { "" }
        }
    }
}