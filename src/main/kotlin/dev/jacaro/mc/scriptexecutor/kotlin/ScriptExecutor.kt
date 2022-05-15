package dev.jacaro.mc.scriptexecutor.kotlin

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import dev.jacaro.mc.scriptexecutor.kotlin.automation.config.AutomationConfigManager
import dev.jacaro.mc.scriptexecutor.kotlin.commands.CommandSEAutomation
import dev.jacaro.mc.scriptexecutor.kotlin.commands.CommandScriptExecute
import dev.jacaro.mc.scriptexecutor.kotlin.coroutines.asyncCoroutineScope
import dev.jacaro.mc.scriptexecutor.kotlin.coroutines.ioCoroutineScope
import dev.jacaro.mc.scriptexecutor.kotlin.coroutines.synchronousCoroutineScope
import dev.jacaro.mc.scriptexecutor.kotlin.storage.Storage
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.IOException
import java.util.logging.Level

class ScriptExecutor : JavaPlugin() {

    lateinit var automationConfig: FileConfiguration
    lateinit var automationFile: File

    override fun onEnable() {
        plugin = this
        config.options().copyDefaults(false)
        this.saveDefaultConfig()
        reloadConfig()

        setupAutomationConfig()
        loadAutomationScripts()

        CommandScriptExecute.initialize()
        CommandSEAutomation.initialize()

        logger.log(Level.INFO, "ScriptExecutor Enabled")
    }

    override fun onDisable() {
        if (Storage.runningScripts.size > 0) {
            logger.log(Level.INFO, "Scripts actively running. Ending running processes.")
            for (runningScript in Storage.runningScripts) {
                runBlocking {
                    val process = runningScript.process
                    process.destroy()
                    delay(200)
                    if (process.isAlive) {
                        logger.log(Level.INFO,
                            "Process for running script \"${runningScript.id}\" is taking longer to stop.")
                        delay(2000)
                        if (process.isAlive) {
                            process.destroyForcibly()
                            logger.log(Level.WARNING,
                                "Process for running script \"${runningScript.id}\" has been stopped forcibly.")
                        }
                    }
                }
            }

            synchronousCoroutineScope.cancel()
            asyncCoroutineScope.cancel()
            ioCoroutineScope.cancel()
        }

        logger.log(Level.INFO, "ScriptExecutor Disabled")
    }

    private fun setupAutomationConfig() {
        automationFile = File(dataFolder, "automation.yml")
        if (!automationFile.exists()) {
            logger.log(Level.INFO, "Creating Automation Config File")
            automationFile.parentFile.mkdirs()
            saveResource("automation.yml", false)
        }

        automationConfig = YamlConfiguration()
        try {
            automationConfig.load(automationFile)
        } catch (ex: IOException) {
            throw ex
        }
    }

    private fun loadAutomationScripts() {
        logger.log(Level.INFO, "Loading Automation Scripts")
        for (it in AutomationConfigManager.automatedScriptNames) {
            AutomationConfigManager.loadScript(it)
            logger.log(Level.INFO, "Script $it loaded.")
        }
    }

    companion object {
        lateinit var plugin: ScriptExecutor
        private set
    }
}
