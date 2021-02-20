package parts.lost.mc.scriptexecutor.kotlin.automation.config

import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
import parts.lost.mc.scriptexecutor.kotlin.automation.config.versions.AutomationConfig
import parts.lost.mc.scriptexecutor.kotlin.automation.config.versions.AutomationConfig1
import java.lang.RuntimeException


val AutomationConfigManager: AutomationConfig = when(ScriptExecutor.plugin.automationConfig.getInt("version")) {
    1 -> AutomationConfig1
    else -> throw RuntimeException("Invalid config version")
}