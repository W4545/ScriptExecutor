package dev.jacaro.mc.scriptexecutor.kotlin.automation.config

import dev.jacaro.mc.scriptexecutor.kotlin.ScriptExecutor
import dev.jacaro.mc.scriptexecutor.kotlin.automation.config.versions.AutomationConfig
import dev.jacaro.mc.scriptexecutor.kotlin.automation.config.versions.AutomationConfig1
import java.lang.RuntimeException


val AutomationConfigManager: AutomationConfig = when(ScriptExecutor.plugin.automationConfig.getInt("version")) {
    1 -> AutomationConfig1
    else -> throw RuntimeException("Invalid config version")
}