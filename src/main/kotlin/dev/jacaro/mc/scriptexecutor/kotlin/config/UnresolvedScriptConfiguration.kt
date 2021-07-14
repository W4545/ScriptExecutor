package dev.jacaro.mc.scriptexecutor.kotlin.config

import org.bukkit.configuration.ConfigurationSection

internal data class UnresolvedScriptConfiguration(
        val name: String,
        val scheme: String,
        var commands: List<String>? = null,
        var workingDirectory: String? = null,
        var wrapOutput: Boolean = false,
        var logFile: Boolean = false,
        var logFileLocation: String? = null
) {
    fun map(): ScriptConfiguration? {
        return commands?.let { ScriptConfiguration(name, scheme, it, workingDirectory, wrapOutput, logFile, logFileLocation) }
    }

    companion object {
        fun loadValues(configurationSection: ConfigurationSection?, unresolvedScriptConfiguration: UnresolvedScriptConfiguration) {
            configurationSection?.getStringList("commands")?.also {
                if (it.size != 0)
                    unresolvedScriptConfiguration.commands = it
            }

            configurationSection?.getString("workingDirectory")?.also {
                unresolvedScriptConfiguration.workingDirectory = it
            }

            configurationSection?.getString("wrapOutput")?.also {
                unresolvedScriptConfiguration.wrapOutput = it.toBoolean()
            }

            configurationSection?.getString("logFileLocation")?.also {
                unresolvedScriptConfiguration.logFileLocation = it
            }

            configurationSection?.getString("logFile")?.also {
                unresolvedScriptConfiguration.logFile = it.toBoolean()
            }
        }
    }
}