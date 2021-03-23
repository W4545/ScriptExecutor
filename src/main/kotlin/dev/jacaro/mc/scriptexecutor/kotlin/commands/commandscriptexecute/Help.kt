package dev.jacaro.mc.scriptexecutor.kotlin.commands.commandscriptexecute

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import dev.jacaro.mc.scriptexecutor.kotlin.commands.CommandScriptExecute

object Help: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when {
            args.size == 1 -> {
                when (args[0]) {
                    "exec" -> {
                        sender.sendMessage("Description: Executes the provided script")
                        sender.sendMessage("Usage: /$label exec ${ChatColor.ITALIC}<script>${ChatColor.RESET} <optional configuration> <optional arguments..>")
                    }
                    "help" -> {
                        sender.sendMessage("Description: Provides help with subcommands")
                        sender.sendMessage("Usage: /$label help ${ChatColor.ITALIC}<optional subcommand>${ChatColor.RESET}")
                    }
                    "list" -> {
                        sender.sendMessage("Description: Lists all registered scripts")
                        sender.sendMessage("Usage: /$label list")
                    }
                    "running" -> {
                        sender.sendMessage("Description: Lists all running scripts")
                        sender.sendMessage("Usage: /$label running ${ChatColor.ITALIC}<optional script>${ChatColor.RESET}")
                    }
                    "reload" -> {
                        sender.sendMessage("Description: Reloads configuration from config file. All scripts must be stopped.")
                        sender.sendMessage("Usage: /$label reload")
                    }
                    "cancel" -> {
                        sender.sendMessage("Description: Requests the provided script to be terminated. " +
                                "The script will attempt to shutdown properly.")
                        sender.sendMessage("Usage: /$label cancel <script>")
                    }
                    "testconfiguration" -> {
                        sender.sendMessage("Description: Generates a script configuration for the given script " +
                                "and optional configuration.")
                        sender.sendMessage("Usage: /$label testconfiguration <script> <optional configuration>")
                    }
                    else -> sender.sendMessage("This is not a valid subcommand. Do /$label help for more info.")
                }
            }
            args.isEmpty() -> {
                sender.sendMessage("Valid subcommands: ${CommandScriptExecute.subCommands.joinToString(", ")}")
                sender.sendMessage("Do /$label help <subcommand> for more info.")
            }
            else -> sender.sendMessage("${ChatColor.RED}This command takes one optional argument. See /$label help for details.")
        }

        return true
    }
}