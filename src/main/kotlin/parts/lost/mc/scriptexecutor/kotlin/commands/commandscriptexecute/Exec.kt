package parts.lost.mc.scriptexecutor.kotlin.commands.commandscriptexecute

import kotlinx.coroutines.*
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
import parts.lost.mc.scriptexecutor.kotlin.config.ConfigManager
import parts.lost.mc.scriptexecutor.kotlin.config.ScriptConfiguration
import parts.lost.mc.scriptexecutor.kotlin.coroutines.async
import parts.lost.mc.scriptexecutor.kotlin.exceptions.ScriptExecutorConfigException
import parts.lost.mc.scriptexecutor.kotlin.storage.RunningScript
import parts.lost.mc.scriptexecutor.kotlin.storage.Storage
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.Level

object Exec: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val script: ScriptConfiguration? = when(args.size) {
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

        val commandArgs: Array<out String> = when(args.size) {
            1, 2 -> emptyArray()
            else -> args.copyOfRange(2, args.size)
        }

        if (script == null) {
            sender.sendMessage("${ChatColor.RED}Unable to locate script entry \"${args[0]}\" with " +
                    "scheme \"${args[1]}\" in config")
        } else {
            val scriptID = Storage.scriptName(script.name)
            val processBuilder = ProcessBuilder(script.commands + commandArgs)
            val logFile = if (script.logFile) {
                processBuilder.redirectError(processBuilder.redirectInput())
                processBuilder.redirectErrorStream(true)
                val formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm:ss")
                val formatted = LocalDateTime.now().format(formatter)
                File(script.logFileLocation ?: throw ScriptExecutorConfigException("")).mkdirs()
                File("${script.logFileLocation}/${formatted}-$scriptID.log")
            } else null

            if (script.workingDirectory != null)
                processBuilder.directory(File(script.workingDirectory))
            sender.sendMessage("[$scriptID] Script execution starting.")

            try {
                val process =processBuilder.start()

                val job = if (script.wrapOutput || logFile != null) {
                    GlobalScope.launch(Dispatchers.IO) {
                        try {
                            BufferedReader(InputStreamReader(process.inputStream)).use { bufferedReader ->
                                if (logFile != null) {
                                    BufferedWriter(FileWriter(logFile)).use {
                                        if (ConfigManager.verbose) {
                                            if (script.wrapOutput)
                                                sender.sendMessage(script.verbose)
                                            it.appendln(script.verbose)
                                        }
                                        var line: String? = ""
                                        while (line != null) {
                                            line = bufferedReader.readLine()
                                            if (line != null) {
                                                if (script.wrapOutput)
                                                    sender.sendMessage("${ChatColor.DARK_GRAY}[${scriptID}] $line")
                                                it.appendln(line)
                                            }
                                        }
                                    }
                                } else {
                                    if (ConfigManager.verbose)
                                        sender.sendMessage(script.verbose)
                                    var line: String? = ""
                                    while (line != null) {
                                        line = bufferedReader.readLine()
                                        if (line != null) {
                                            sender.sendMessage("${ChatColor.DARK_GRAY}[${scriptID}] $line")
                                        }
                                    }
                                }
                            }
                            process.waitFor()
                        } catch (ex: IOException) {
                            sender.server.consoleSender.sendMessage("IO Worker for script $scriptID has been closed")
                        }
                    }
                } else null

                val runningScript = RunningScript(scriptID, process, job, script)
                Storage.runningScripts.add(runningScript)


                val isAlive = GlobalScope.launch(Dispatchers.async) {
                    while (process.isAlive)
                        delay(1000L)
                    runningScript.inputJob?.join()
                    sender.sendMessage("${ChatColor.BLUE}[${runningScript.id}] Script execution completed.")
                    val success = Storage.runningScripts.remove(runningScript)
                    if (!success)
                        sender.sendMessage("${ChatColor.RED}Failed to remove running script from plugin storage.")
                }

                runningScript.isAliveJob = isAlive
            } catch (ex: IOException) {
                sender.sendMessage("${ChatColor.RED}An exception occurred while launching the process. The provided " +
                        "working directory or commands are incorrect.")
                ScriptExecutor.plugin.logger.log(Level.WARNING, "Unexpected error when launching process.", ex)
            }
        }

        return true
    }
}
