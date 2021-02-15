package parts.lost.mc.scriptexecutor.kotlin.commands

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
import parts.lost.mc.scriptexecutor.kotlin.commands.commandseautomation.Create
import parts.lost.mc.scriptexecutor.kotlin.interfaces.CommandInitializer

object CommandSEAutomation : CommandInitializer, TabCompleter, CommandExecutor {
    private val subCommands = listOf("create")

    override fun initialize() {
        val command = ScriptExecutor.plugin.getCommand("seautomate")!!
        command.setExecutor(this)
        command.tabCompleter = this
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return if (args.size == 1)
            subCommands.toMutableList()
        else if (args.size > 1)
            subCommands.find { it.startsWith(args[0]) }
        else MutableList(0) { "" }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage("${ChatColor.GOLD}ScriptExecutor running version: ${ChatColor.BLUE}${ScriptExecutor.plugin.description.version}")
            sender.sendMessage("See /$label help for available subcommands.")
        } else {
            when (args[0]) {
                "create" -> Create.onCommand(sender, command, label, args.copyOfRange(1, args.size))
                else -> sender.sendMessage("${ChatColor.RED}Please provide a valid subcommand. See /$label help for details")
            }
        }

        return true
    }
}