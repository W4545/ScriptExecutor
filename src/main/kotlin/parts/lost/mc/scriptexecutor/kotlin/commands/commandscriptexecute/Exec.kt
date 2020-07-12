package parts.lost.mc.scriptexecutor.kotlin.commands.commandscriptexecute

import kotlinx.coroutines.*
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import parts.lost.mc.scriptexecutor.kotlin.config.ConfigManager
import java.io.BufferedReader
import java.io.InputStreamReader

object Exec: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when {
            args.size == 1 -> {

                val script = ConfigManager.getScripts().find { it.first == args[0] }

                if (script == null) {
                    sender.sendMessage("${ChatColor.RED}Unable to locate script entry in config")
                    sender.sendMessage(ConfigManager.getScripts().toString())
                } else {
                    val processBuilder = ProcessBuilder(script.second)
                    sender.sendMessage("Script execution started.")
                    val process = processBuilder.start()

                    val job = GlobalScope.launch(Dispatchers.IO) {

                        val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
                        var line: String? = ""
                        while (line != null) {
                            line = bufferedReader.readLine()
                            if (line != null)
                                sender.sendMessage(line)
                        }
                        bufferedReader.close()
                        process.waitFor()
                        sender.sendMessage("Script execution completed.")
                    }
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