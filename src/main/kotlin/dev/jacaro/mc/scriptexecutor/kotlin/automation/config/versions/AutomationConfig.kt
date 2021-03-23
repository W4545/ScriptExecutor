package dev.jacaro.mc.scriptexecutor.kotlin.automation.config.versions

import dev.jacaro.mc.scriptexecutor.kotlin.storage.AutomatedScript


interface AutomationConfig {

    val version : Int

    val automatedScriptNames : List<String>

    fun loadScript(string: String) : AutomatedScript

    fun writeScript(automatedScript: AutomatedScript)

    fun deleteScript(string: String)
}