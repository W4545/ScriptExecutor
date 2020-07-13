package parts.lost.mc.scriptexecutor.kotlin.config

import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
import java.util.*

object ConfigManager {

    lateinit var plugin: ScriptExecutor

    fun getScriptNames(): List<String> {
        return plugin.config.getConfigurationSection("scripts")?.getValues(false)?.map { it.key }
                ?: Collections.emptyList()
    }

    fun getVersion(): Int = plugin.config.getInt("version")

    fun scripts(): List<ScriptConfiguration> {
        return getScriptNames().map { getScript(it)!! }
    }

    fun getScript(name: String): ScriptConfiguration? {
        val configurationSection = plugin.config.getConfigurationSection("scripts.$name") ?: return null

        val commands: List<String> = configurationSection.getStringList("commands")

        val workingDirectory = configurationSection.getString("workingDirectory")

        val wrapOutput = configurationSection.getBoolean("wrapOutput")

        return ScriptConfiguration(name, commands, workingDirectory, wrapOutput)
    }
}