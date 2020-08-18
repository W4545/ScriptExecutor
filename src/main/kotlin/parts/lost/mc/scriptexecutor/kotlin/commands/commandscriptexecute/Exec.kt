package parts.lost.mc.scriptexecutor.kotlin.commands.commandscriptexecute

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import parts.lost.mc.scriptexecutor.kotlin.config.ConfigManager
import parts.lost.mc.scriptexecutor.kotlin.config.ScriptConfiguration
import parts.lost.mc.scriptexecutor.kotlin.scriptrunner.CreateScript

object Exec: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val script: ScriptConfiguration? = when (args.size) {
            2 -> ConfigManager.getScript(args[0], args[1])
            1 -> {
                if (sender is Player && ConfigManager.getScriptSchemeConfigurations(args[0]).contains("player"))
                    ConfigManager.getScript(args[0], "player")
                else if (sender is ConsoleCommandSender && ConfigManager.getScriptSchemeConfigurations(args[0]).contains("console"))
                    ConfigManager.getScript(args[0], "console")
                else
                    ConfigManager.getScript(args[0], "default")
            }
            0 -> {
                sender.sendMessage("${ChatColor.RED}This command requires an argument")
                return true
            }
            else -> {
                ConfigManager.getScript(args[0], args[1])
            }
        }

        val commandArgs: Array<out String> = when (args.size) {
            1, 2 -> emptyArray()
            else -> args.copyOfRange(2, args.size)
        }

        if (script == null)
            sender.sendMessage("${ChatColor.RED}Unable to locate script entry \"${args[0]}\" with " +
                    "scheme \"${args[1]}\" in config")
        else
            CreateScript.create(script, commandArgs, sender)

        return true
    }
}
