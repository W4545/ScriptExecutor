package parts.lost.mc.scriptexecutor.kotlin.config

import org.bukkit.configuration.file.FileConfiguration
import java.lang.IllegalArgumentException
import java.util.*

object ConfigManager {

    lateinit var config: FileConfiguration

    fun getScriptNames(): List<String> {
        return config.getConfigurationSection("scripts")?.getValues(false)?.map { it.key }
                ?: Collections.emptyList()
    }

    fun getVersion(): Int = config.getInt("version")

    fun scripts(): List<ScriptConfiguration> {
        return getScriptNames().map { getScript(it)!! }
    }

    fun getScript(name: String): ScriptConfiguration? {
        val configurationSection = config.getConfigurationSection("scripts.$name") ?: return null

        var commands: List<String> = configurationSection.getStringList("commands")

        if (commands.isEmpty())
            commands = listOf(configurationSection.getString("command")!!)

        val workingDirectory = configurationSection.getString("workingDirectory")

        val wrapOutput = configurationSection.getBoolean("wrapOutput")

        return ScriptConfiguration(name, commands, workingDirectory, wrapOutput)
    }
}