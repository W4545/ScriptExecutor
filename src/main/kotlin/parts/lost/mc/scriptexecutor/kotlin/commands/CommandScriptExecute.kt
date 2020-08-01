package parts.lost.mc.scriptexecutor.kotlin.commands


import org.bukkit.ChatColor
import org.bukkit.command.*
import org.bukkit.entity.Player
import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
import parts.lost.mc.scriptexecutor.kotlin.commands.commandscriptexecute.*
import parts.lost.mc.scriptexecutor.kotlin.config.ConfigManager
import parts.lost.mc.scriptexecutor.kotlin.interfaces.CommandInitializer
import parts.lost.mc.scriptexecutor.kotlin.storage.Storage
import java.util.Collections

object CommandScriptExecute : CommandExecutor, TabCompleter, CommandInitializer {

    val subCommands = listOf("exec", "help", "list", "running", "reload", "cancel", "testconfiguration")

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (args.isEmpty()) {
            sender.sendMessage("${ChatColor.GOLD}ScriptExecutor running version: ${ChatColor.BLUE}${ScriptExecutor.plugin.description.version}")
            sender.sendMessage("See /$label help for available subcommands.")
        } else {
            when(args[0]) {
                "exec" -> Exec.onCommand(sender, command, label, args.copyOfRange(1, args.size))
                "help" -> Help.onCommand(sender, command, label, args.copyOfRange(1, args.size))
                "list" -> SubCommandList.onCommand(sender, command, label, args.copyOfRange(1, args.size))
                "running" -> Running.onCommand(sender, command, label, args.copyOfRange(1, args.size))
                "reload" -> Reload.onCommand(sender, command, label, args.copyOfRange(1, args.size))
                "cancel" -> Cancel.onCommand(sender, command, label, args.copyOfRange(1, args.size))
                "testconfiguration" -> TestConfiguration.onCommand(sender, command, label, args.copyOfRange(1, args.size))
                else -> sender.sendMessage("${ChatColor.RED}Please provide a valid subcommand. See /$label help for details")
            }
        }

        return true
    }

    fun infer(sender: CommandSender, args: Array<out String>): String {
        return if (sender is Player && ConfigManager.getScriptSchemeConfigurations(args[0]).contains("player"))
                "player"
            else if (sender is ConsoleCommandSender && ConfigManager.getScriptSchemeConfigurations(args[0]).contains("console"))
                "console"
            else
                "default"
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return if (args.size == 1 || (args.size == 2 && args[0] == "help"))
            subCommands.toMutableList()
        else if (args.size == 2 && (args[0] == "exec" || args[0] == "testconfiguration"))
            ConfigManager.getScriptNames().toMutableList()
        else if (args.size == 2 && (args[0] == "running" || args[0] == "cancel"))
            Storage.runningScripts.map { it.id }.toMutableList()
        else if (args.size == 3 && (args[0] == "exec" || args[0] == "testconfiguration"))
            ConfigManager.getScriptSchemeConfigurations(args[1]).toMutableList()
        else
            Collections.emptyList()
    }

    override fun initialize() {
        val command = ScriptExecutor.plugin.getCommand("scriptexecute")!!
        command.tabCompleter = this
        command.setExecutor(this)
    }
}
