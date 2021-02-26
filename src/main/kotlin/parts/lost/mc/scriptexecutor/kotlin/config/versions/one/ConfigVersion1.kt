package parts.lost.mc.scriptexecutor.kotlin.config.versions.one

import org.bukkit.configuration.ConfigurationSection
import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
import parts.lost.mc.scriptexecutor.kotlin.config.ScriptConfiguration
import parts.lost.mc.scriptexecutor.kotlin.config.UnresolvedScriptConfiguration
import parts.lost.mc.scriptexecutor.kotlin.config.versions.ConfigVersion
import parts.lost.mc.scriptexecutor.kotlin.exceptions.ScriptExecutorConfigException
import java.util.*

object ConfigVersion1: ConfigVersion {

    override val configVersion: Int = 1

    override val verbose: Boolean
        get() = ScriptExecutor.plugin.config.getBoolean("verbose")

    override val timeZoneOverride: String?
        get() = ScriptExecutor.plugin.config.getString("timezone")

    override fun getScriptNames(): List<String> {
        return ScriptExecutor.plugin.config.getConfigurationSection("Scripts")?.getKeys(false)?.toMutableList()
                ?: Collections.emptyList()
    }

    override fun getScriptSchemeConfigurations(script: String): List<String> {
        val scriptConfig = ScriptExecutor.plugin.config.getConfigurationSection("Scripts.$script")
        val configurations = scriptConfig?.getConfigurationSection("configurations")
            ?.getValues(false)?.map { it.key }
            ?: Collections.emptyList()

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

    internal fun loadValues(configurationSection: ConfigurationSection?, unresolvedScriptConfiguration: UnresolvedScriptConfiguration) {

        configurationSection?.getStringList("commands")?.also {
            if (it.size != 0)
                unresolvedScriptConfiguration.commands = it
        }
        configurationSection?.getString("workingDirectory")?.also { unresolvedScriptConfiguration.workingDirectory = it }

        configurationSection?.getString("wrapOutput")?.also { unresolvedScriptConfiguration.wrapOutput = it.toBoolean() }
        configurationSection?.getString("logFileLocation")?.also { unresolvedScriptConfiguration.logFileLocation = it }
        configurationSection?.getString("logFile")?.also { unresolvedScriptConfiguration.logFile = it.toBoolean() }
    }

    override fun getScript(name: String, configurationScheme: String): ScriptConfiguration? {
        val unresolvedScriptConfiguration = UnresolvedScriptConfiguration(name, configurationScheme)
        val config = ScriptExecutor.plugin.config
        loadValues(config.getConfigurationSection("Defaults"), unresolvedScriptConfiguration)

        val scriptRoot = config.getConfigurationSection("Scripts.$name")
        scriptRoot?.getStringList("commands")?.also { unresolvedScriptConfiguration.commands = it }
        loadValues(scriptRoot?.getConfigurationSection("default"), unresolvedScriptConfiguration)
        loadValues(scriptRoot?.getConfigurationSection("configurations.$configurationScheme"), unresolvedScriptConfiguration)


        return unresolvedScriptConfiguration.map()
    }
}
