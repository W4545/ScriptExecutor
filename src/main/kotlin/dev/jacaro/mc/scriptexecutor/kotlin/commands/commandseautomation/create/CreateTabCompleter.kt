package dev.jacaro.mc.scriptexecutor.kotlin.commands.commandseautomation.create

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import dev.jacaro.mc.scriptexecutor.kotlin.config.ConfigManager
import dev.jacaro.mc.scriptexecutor.kotlin.endsWith


private val digits = arrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '0')

object CreateTabCompleter : TabCompleter {
    override fun onTabComplete(
        sender: CommandSender, command: Command, alias: String, args: Array<out String>
    ): MutableList<String> = when (args.size) {
        2 -> ConfigManager.getScriptNames().toMutableList()
        3 -> ConfigManager.getScriptSchemeConfigurations(args[1]).toMutableList()
        4 -> {
            if (args[3].endsWith(digits)) {
                if (args[3].contains('-')) {
                    if (args[3].count { it == '-' } < 2)
                        mutableListOf(args[3] + "-")
                    else
                        MutableList(0) { "" }
                } else {
                    if (args[3].all { it.isDigit() } && args[3].length == 4)
                        arrayOf("-", "s", "m", "h", "d").map { args[3] + it }.toMutableList()
                    else
                        arrayOf("s", "m", "h", "d").map { args[3] + it }.toMutableList()
                }
            }
            else MutableList(0) { "" }
        }
        5 -> {
            if (args[4].endsWith(digits) && args[4].count { it == ':' } == 0)
                arrayOf("-", "s", "m", "h", "d", ":").map { args[4] + it }.toMutableList()
            else
                MutableList(0) { "" }
        }
        6 -> {
            if (args[5].endsWith(digits))
                arrayOf("s", "m", "h", "d").map { args[5] + it }.toMutableList()
            else
                MutableList(0) { "" }
        }
        else -> MutableList(0) { "" }
    }
}