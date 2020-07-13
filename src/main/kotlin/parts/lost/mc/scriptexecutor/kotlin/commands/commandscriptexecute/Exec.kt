package parts.lost.mc.scriptexecutor.kotlin.commands.commandscriptexecute

import kotlinx.coroutines.*
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import parts.lost.mc.scriptexecutor.kotlin.config.ConfigManager
import parts.lost.mc.scriptexecutor.kotlin.coroutines.async
import parts.lost.mc.scriptexecutor.kotlin.storage.RunningScript
import parts.lost.mc.scriptexecutor.kotlin.storage.Storage
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object Exec: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when {
            args.size == 1 -> {

                val script = ConfigManager.getScript(args[0])

                if (script == null) {
                    sender.sendMessage("${ChatColor.RED}Unable to locate script entry in config")
                } else {
                    val processBuilder = ProcessBuilder(script.commands)
                    if (script.workingDirectory != null)
                        processBuilder.directory(File(script.workingDirectory))
                    val scriptID = Storage.scriptName(script.name)
                    sender.sendMessage("[$scriptID] Script execution starting.")
                    val process = processBuilder.start()

                    val job = if (script.wrapOutput) {
                        GlobalScope.launch(Dispatchers.IO) {
                            BufferedReader(InputStreamReader(process.inputStream)).use { bufferedReader ->
                                var line: String? = ""
                                while (line != null) {
                                    line = bufferedReader.readLine()
                                    if (line != null)
                                        sender.sendMessage(line)
                                }
                                bufferedReader.close()
                                process.waitFor()
                            }
                        }
                    } else null

                    val runningScript = RunningScript(scriptID, process, job, script)
                    Storage.runningScripts.add(runningScript)


                    val isAlive = GlobalScope.launch(Dispatchers.async) {
                        delay(5000L)
                        while (process.isAlive)
                            delay(1000L)
                        if (runningScript.inputJob?.isActive == true)
                            runningScript.inputJob.cancel()
                        sender.sendMessage("Script execution completed.")
                        val success = Storage.runningScripts.remove(runningScript)
                        if (!success)
                            sender.sendMessage("${ChatColor.RED}Failed to remove running script from plugin storage.")
                    }

                    runningScript.isAliveJob = isAlive
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