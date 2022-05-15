package dev.jacaro.mc.scriptexecutor.kotlin.commands.commandscriptexecute

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import dev.jacaro.mc.scriptexecutor.kotlin.commands.CommandScriptExecute
import dev.jacaro.mc.scriptexecutor.kotlin.config.ConfigManager
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.BasicHelpNotes
import dev.jacaro.mc.scriptexecutor.kotlin.emptyMutableList
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.SubCommand

object TestConfiguration: SubCommand {
    override val name = "testconfiguration"
    override val helpNotes = BasicHelpNotes(
        this,
        "<script> ${ChatColor.ITALIC}<optional configuration>${ChatColor.RESET}",
        "Generates a script configuration for the given script and optional configuration."
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        val script = when (args.size) {
            2 -> {
                ConfigManager.getScript(args[0], args[1])
            }
            1 -> {
                ConfigManager.getScript(args[0], CommandScriptExecute.infer(sender, args))
            }
            else -> {
                sender.sendMessage("${ChatColor.RED}This command takes one or two arguments." +
                        "See /$label help testconfiguration")
                return true
            }
        }
        sender.sendMessage(script?.verbose ?: "${ChatColor.RED} Unable to find or infer configuration.")

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        return when (args.size) {
            2 -> ConfigManager.getScriptNames().toMutableList()
            3 -> ConfigManager.getScriptSchemeConfigurations(args[1]).toMutableList()
            else -> emptyMutableList()
        }
    }
}