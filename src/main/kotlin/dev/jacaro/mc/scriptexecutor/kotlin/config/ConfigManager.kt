package dev.jacaro.mc.scriptexecutor.kotlin.config

import dev.jacaro.mc.scriptexecutor.kotlin.ScriptExecutor
import dev.jacaro.mc.scriptexecutor.kotlin.config.versions.ConfigVersion
import dev.jacaro.mc.scriptexecutor.kotlin.config.versions.one.ConfigVersion1
import java.lang.RuntimeException

val ConfigManager: ConfigVersion = when(ScriptExecutor.plugin.config.getInt("version")) {
    1 -> ConfigVersion1("Scripts", "Defaults")
    2 -> ConfigVersion1("scripts", "defaults", 2)
    else -> throw RuntimeException("Invalid config version")
}
