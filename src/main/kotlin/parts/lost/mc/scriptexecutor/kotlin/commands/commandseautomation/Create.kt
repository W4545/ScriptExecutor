package parts.lost.mc.scriptexecutor.kotlin.commands.commandseautomation

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import parts.lost.mc.scriptexecutor.kotlin.config.ConfigManager
import parts.lost.mc.scriptexecutor.kotlin.endsWith
import parts.lost.mc.scriptexecutor.kotlin.interfaces.HelpNotes
import parts.lost.mc.scriptexecutor.kotlin.interfaces.SubCommand

object Create: SubCommand {
    private val digits = arrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '0')

    override val name = "create"
    override val helpNotes = object : HelpNotes {
        override fun onCommand(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<out String>
        ): Boolean {
            sender.sendMessage("Description: " +
                    "Creates a new automation request with the given delay or datetime, and optional period")
            sender.sendMessage("Examples: /$label create hello 1m 10s " +
                    "(create an automation script that runs every 10 seconds starting in 1 minute.)")
            sender.sendMessage("Examples: /$label create hello 2021-12-1 14:30 " +
                    "(creates an automation script that runs on the date and time provided)")
            sender.sendMessage("Usage: /$label create <script> <delay or date> <optional time> <optional period>")

            return true
        }

    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        TODO("Not yet implemented")
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        return when (args.size) {
            2 -> ConfigManager.getScriptNames().toMutableList()
            3 -> {
                if (args[2].endsWith(digits)) {
                    if (args[2].contains('-')) {
                        if (args[2].count { it == '-' } < 2)
                            mutableListOf(args[2] + "-")
                        else
                            MutableList(0) { "" }
                    } else {
                        if (args[2].all { it.isDigit() } && args[2].length == 4)
                            arrayOf("-", "s", "m", "h", "d").map { args[2] + it }.toMutableList()
                        else
                            arrayOf("s", "m", "h", "d").map { args[2] + it }.toMutableList()
                    }
                }
                else MutableList(0) { "" }
            }
            4 -> {
                if (args[3].endsWith(digits))
                    arrayOf("-", "s", "m", "h", "d").map { args[3] + it }.toMutableList()
                else
                    MutableList(0) { "" }
            }
            else -> MutableList(0) { "" }
        }
    }
}