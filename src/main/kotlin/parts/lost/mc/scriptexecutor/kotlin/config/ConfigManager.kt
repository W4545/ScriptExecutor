package parts.lost.mc.scriptexecutor.kotlin.config

import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
import parts.lost.mc.scriptexecutor.kotlin.config.versions.Version
import parts.lost.mc.scriptexecutor.kotlin.config.versions.one.Version1
import java.lang.RuntimeException

val ConfigManager: Version = when(ScriptExecutor.plugin.config.getInt("version")) {
    1 -> Version1
    else -> throw RuntimeException("Invalid config version")
}
