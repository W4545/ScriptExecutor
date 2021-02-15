package parts.lost.mc.scriptexecutor.kotlin.constructs

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
import parts.lost.mc.scriptexecutor.kotlin.interfaces.CommandInitializer
import parts.lost.mc.scriptexecutor.kotlin.interfaces.SubCommand


abstract class CommandManager : SubCommand, CommandInitializer {
    abstract val subCommands: List<SubCommand>

    override fun initialize() {
        val command = ScriptExecutor.plugin.getCommand(name)!!
        command.setExecutor(this)
        command.tabCompleter = this
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>)
    : MutableList<String> {
        return when {
            args.size == 1 -> subCommands.map { it.name }.toMutableList()
            args.size > 1 -> subCommands.find { it.name == args[0] }
                ?.onTabComplete(sender, command, alias, args) ?: MutableList(0) { "" }
            else -> MutableList(0) { "" }
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (args.isEmpty()) {
            sender.sendMessage("${ChatColor.GOLD}ScriptExecutor running version: ${ChatColor.BLUE}${ScriptExecutor.plugin.description.version}")
            sender.sendMessage("See /$label help for available subcommands.")
        } else {
            subCommands.find { it.name == args[0] }
                ?.onCommand(sender, command, label, args.copyOfRange(1, args.size))
                ?: sender.sendMessage("${ChatColor.RED}Please provide a valid subcommand. See /$label help for details")
        }

        return true
    }
}