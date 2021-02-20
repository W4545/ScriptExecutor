package parts.lost.mc.scriptexecutor.kotlin

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import parts.lost.mc.scriptexecutor.kotlin.commands.CommandSEAutomation
import parts.lost.mc.scriptexecutor.kotlin.commands.CommandScriptExecute
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

        CommandScriptExecute.initialize()
        CommandSEAutomation.initialize()

        logger.log(Level.INFO, "ScriptExecutor Enabled")
    }

    override fun onDisable() {
        logger.log(Level.INFO, "ScriptExecutor Disabled")
    }

    private fun setupAutomationConfig() {
        automationFile = File(dataFolder, "automation.yml")
        if (!automationFile.exists()) {
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

    companion object {
        lateinit var plugin: ScriptExecutor
        private set
    }
}
