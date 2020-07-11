package parts.lost.mc.scriptexecutor.kotlin.commands.commandscriptexecute

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import parts.lost.mc.scriptexecutor.kotlin.config.ConfigManager

object Exec: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when {
            args.size == 1 -> {

                val script = ConfigManager.getScripts().find { it.first == args[0] }

                if (script == null) {
                    sender.sendMessage("${ChatColor.RED}Unable to locate script entry in config")
                    sender.sendMessage(ConfigManager.getScripts().toString())
                } else {
                    // val process = Runtime.getRuntime().exec(script.second)

//                    runBlocking {
//                        launch(Dispatchers.Main) {
//                            process.inputStream
//                        }
//                    }
                    sender.sendMessage("${ChatColor.BLUE}${script.second}")
                    sender.sendMessage("Script execution started.")
                }

            }
            args.isEmpty() -> {
                sender.sendMessage("${ChatColor.RED}This command requires an argument")
            }
            else -> {
                sender.sendMessage("${ChatColor.RED}This command accepts only one argument")
            }
        }

        return true
    }
}