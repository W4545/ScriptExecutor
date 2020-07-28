package parts.lost.mc.scriptexecutor.kotlin.config

import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
import parts.lost.mc.scriptexecutor.kotlin.config.versions.ConfigVersion
import parts.lost.mc.scriptexecutor.kotlin.config.versions.one.ConfigVersion1
import java.lang.RuntimeException

val ConfigManager: ConfigVersion = when(ScriptExecutor.plugin.config.getInt("version")) {
    1 -> ConfigVersion1
    else -> throw RuntimeException("Invalid config version")
}
