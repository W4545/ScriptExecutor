package parts.lost.mc.scriptexecutor.kotlin.automation.config.versions

import parts.lost.mc.scriptexecutor.kotlin.storage.AutomatedScript


interface AutomationConfig {

    val version : Int

    val automatedScriptNames : List<String>

    fun loadScript(string: String) : AutomatedScript
}