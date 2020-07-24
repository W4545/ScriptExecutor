package parts.lost.mc.scriptexecutor.kotlin.config.versions.one

import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
import parts.lost.mc.scriptexecutor.kotlin.config.ScriptConfiguration
import parts.lost.mc.scriptexecutor.kotlin.config.versions.Version
import parts.lost.mc.scriptexecutor.kotlin.exceptions.ScriptExecutorConfigException
import java.util.*

object Version1: Version {
    override fun getScriptNames(): List<String> {
        return ScriptExecutor.plugin.config.getConfigurationSection("scripts")?.getValues(false)?.map { it.key }
                ?: Collections.emptyList()
    }

    override fun getScriptSchemeConfigurations(script: String): List<String> {
        return ScriptExecutor.plugin.config.getConfigurationSection("scripts")?.getConfigurationSection(script)
                ?.getConfigurationSection("configurations")?.getValues(false)?.map { it.key }
                ?: Collections.emptyList()
    }

    override val configVersion: Int = 1

    override fun getScriptsFromScheme(configurationScheme: String): List<ScriptConfiguration> {
        return getScriptNames().map { getScript(it, configurationScheme) ?: throw ScriptExecutorConfigException(it) }
    }

    override fun getScripts(): List<ScriptConfiguration> {
        return getScriptNames().flatMap { script ->
            getScriptSchemeConfigurations(script).map {
                getScript(script, it) ?: throw ScriptExecutorConfigException(script)
            }
        }
    }

    override fun getScript(name: String, configurationScheme: String): ScriptConfiguration? {
        val rootConfigurationSection = ScriptExecutor.plugin.config.getConfigurationSection("scripts.$name")
                ?: return null

        val commands: List<String> = rootConfigurationSection.getStringList("commands")

        val configuredScriptSection = rootConfigurationSection.getConfigurationSection("configurations.$configurationScheme") ?: return null

        val workingDirectory = configuredScriptSection.getString("workingDirectory")

        val wrapOutput = configuredScriptSection.getBoolean("wrapOutput")

        val logFile = configuredScriptSection.getString("logFile")

        return ScriptConfiguration(name, commands, workingDirectory, wrapOutput, logFile)
    }
}
