package dev.jacaro.mc.scriptexecutor.kotlin.config.versions.one

import dev.jacaro.mc.scriptexecutor.kotlin.ScriptExecutor
import dev.jacaro.mc.scriptexecutor.kotlin.config.ScriptConfiguration
import dev.jacaro.mc.scriptexecutor.kotlin.config.UnresolvedScriptConfiguration
import dev.jacaro.mc.scriptexecutor.kotlin.config.UnresolvedScriptConfiguration.Companion.loadValues
import dev.jacaro.mc.scriptexecutor.kotlin.config.versions.ConfigVersion
import dev.jacaro.mc.scriptexecutor.kotlin.exceptions.ScriptExecutorConfigException

class ConfigVersion1(private val scriptsTagName: String, private val defaultsTagName: String, version: Int = 1): ConfigVersion {

    override val configVersion: Int = version

    override val verbose: Boolean
        get() = ScriptExecutor.plugin.config.getBoolean("verbose")

    override val timeZoneOverride: String?
        get() = ScriptExecutor.plugin.config.getString("timezone")

    override fun getScriptNames(): List<String> {
        return ScriptExecutor.plugin.config.getConfigurationSection(scriptsTagName)?.getKeys(false)?.toList()
                ?: emptyList()
    }

    override fun getScriptSchemeConfigurations(script: String): List<String> {
        val scriptConfig = ScriptExecutor.plugin.config.getConfigurationSection("$scriptsTagName.$script")
        val configurations = scriptConfig?.getConfigurationSection("configurations")
            ?.getValues(false)?.map { it.key }
            ?: emptyList()

        return if (scriptConfig?.getConfigurationSection("default") != null)
            configurations.plus("default").sorted()
        else
            configurations.sorted()
    }

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
        val unresolvedScriptConfiguration = UnresolvedScriptConfiguration(name, configurationScheme)
        val config = ScriptExecutor.plugin.config
        loadValues(config.getConfigurationSection(defaultsTagName), unresolvedScriptConfiguration)

        val scriptRoot = config.getConfigurationSection("$scriptsTagName.$name")
        scriptRoot?.getStringList("commands")?.also { unresolvedScriptConfiguration.commands = it }
        loadValues(scriptRoot?.getConfigurationSection("default"), unresolvedScriptConfiguration)
        loadValues(scriptRoot?.getConfigurationSection("configurations.$configurationScheme"), unresolvedScriptConfiguration)


        return unresolvedScriptConfiguration.map()
    }
}
