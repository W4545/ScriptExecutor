package parts.lost.mc.scriptexecutor.kotlin.commands


import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
import parts.lost.mc.scriptexecutor.kotlin.commands.commandscriptexecute.Exec
import parts.lost.mc.scriptexecutor.kotlin.commands.commandscriptexecute.Help
import parts.lost.mc.scriptexecutor.kotlin.commands.commandscriptexecute.Running
import parts.lost.mc.scriptexecutor.kotlin.commands.commandscriptexecute.SubCommandList
import parts.lost.mc.scriptexecutor.kotlin.config.ConfigManager
import parts.lost.mc.scriptexecutor.kotlin.interfaces.CommandInitializer
import parts.lost.mc.scriptexecutor.kotlin.storage.Storage
import java.util.*

object CommandScriptExecute : CommandExecutor, TabCompleter, CommandInitializer {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (args.isEmpty())
            sender.sendMessage("Please provide a subcommand. see /$label help for available subcommands.")
        else {
            when(args[0]) {
                "exec" -> Exec.onCommand(sender, command, label, args.copyOfRange(1, args.size))
                "help" -> Help.onCommand(sender, command, label, args.copyOfRange(1, args.size))
                "list" -> SubCommandList.onCommand(sender, command, label, args.copyOfRange(1, args.size))
                "running" -> Running.onCommand(sender, command, label, args.copyOfRange(1, args.size))
                else -> sender.sendMessage("${ChatColor.RED}Please provide a valid subcommand. See /$label help for details")
            }
        }

        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return if (args.size == 1 || (args.size == 2 && args[0] == "help"))
            mutableListOf("exec", "help", "list", "running")
        else if (args.size == 2 && args[0] == "exec")
            ConfigManager.getScriptNames().toMutableList()
        else if (args.size == 2 && args[0] == "running")
            Storage.runningScripts.map { it.id }.toMutableList()
        else
            Collections.emptyList()
    }

    override fun initialize(plugin: ScriptExecutor) {
        val command = plugin.getCommand("scriptexecute")!!
        command.tabCompleter = this
        command.setExecutor(this)
    }
}