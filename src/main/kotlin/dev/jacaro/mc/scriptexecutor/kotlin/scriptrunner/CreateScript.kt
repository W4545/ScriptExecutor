package dev.jacaro.mc.scriptexecutor.kotlin.scriptrunner

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import dev.jacaro.mc.scriptexecutor.kotlin.ScriptExecutor
import dev.jacaro.mc.scriptexecutor.kotlin.config.ConfigManager
import dev.jacaro.mc.scriptexecutor.kotlin.config.ScriptConfiguration
import dev.jacaro.mc.scriptexecutor.kotlin.coroutines.async
import dev.jacaro.mc.scriptexecutor.kotlin.exceptions.ScriptExecutorConfigException
import dev.jacaro.mc.scriptexecutor.kotlin.storage.RunningScript
import dev.jacaro.mc.scriptexecutor.kotlin.storage.Storage
import kotlinx.coroutines.*
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.Level

object CreateScript {
    @OptIn(DelicateCoroutinesApi::class)
    fun create(script: ScriptConfiguration,
               commandArgs: Array<out String> = emptyArray(),
               sender: CommandSender = ScriptExecutor.plugin.server.consoleSender): String {
        val scriptID = Storage.scriptName(script.name)
        val processBuilder = ProcessBuilder(script.commands + commandArgs)
        val logFile = if (script.logFile) {
            processBuilder.redirectError(processBuilder.redirectInput())
            processBuilder.redirectErrorStream(true)
            val formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH-mm-ss")
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
                                        it.appendLine(script.verbose)
                                    }
                                    var line: String? = ""
                                    while (line != null) {
                                        line = bufferedReader.readLine()
                                        if (line != null) {
                                            if (script.wrapOutput)
                                                sender.sendMessage("${ChatColor.DARK_GRAY}[${scriptID}] $line")
                                            it.appendLine(line)
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
                        ScriptExecutor.plugin.server.consoleSender.sendMessage(
                            "IO Worker for script $scriptID has been closed")
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

        return scriptID
    }
}