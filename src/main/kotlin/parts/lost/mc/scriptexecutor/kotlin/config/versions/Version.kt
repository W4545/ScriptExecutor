package parts.lost.mc.scriptexecutor.kotlin.config.versions

import parts.lost.mc.scriptexecutor.kotlin.config.ScriptConfiguration

interface Version {

    val configVersion: Int

    fun getScriptNames(): List<String>

    fun getScripts(): List<ScriptConfiguration>

    fun getScriptSchemeConfigurations(script: String): List<String>

    fun getScriptsFromScheme(configurationScheme: String = "all"): List<ScriptConfiguration>

    fun getScript(name: String, configurationScheme: String): ScriptConfiguration?
}
