package parts.lost.mc.scriptexecutor.kotlin.config

import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
import java.util.*

object ConfigManager {

    lateinit var plugin: ScriptExecutor

    fun getScriptNames(): List<String> {
        return plugin.config.getConfigurationSection("scripts")?.getValues(false)?.map { it.key }
                ?: Collections.emptyList()
    }

    fun getScriptSchemeConfigurations(script: String): List<String> {
        return plugin.config.getConfigurationSection("scripts")?.getConfigurationSection(script)
                ?.getConfigurationSection("configurations")?.getValues(false)?.map { it.key }
                ?: Collections.emptyList()
    }

    fun getVersion(): Int = plugin.config.getInt("version")

    fun scripts(configurationScheme: String = "all"): List<ScriptConfiguration> {
        return getScriptNames().map { getScript(it, configurationScheme)!! }
    }

    fun getScript(name: String, configurationScheme: String): ScriptConfiguration? {
        val rootConfigurationSection = plugin.config.getConfigurationSection("scripts.$name") ?: return null

        val commands: List<String> = rootConfigurationSection.getStringList("commands")

        val configuredScriptSection = rootConfigurationSection.getConfigurationSection("configurations.$configurationScheme") ?: return null

        val workingDirectory = configuredScriptSection.getString("workingDirectory")

        val wrapOutput = configuredScriptSection.getBoolean("wrapOutput")

        val logFile = configuredScriptSection.getString("logFile")

        return ScriptConfiguration(name, commands, workingDirectory, wrapOutput, logFile)
    }
}