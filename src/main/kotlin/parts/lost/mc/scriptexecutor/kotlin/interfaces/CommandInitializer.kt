package parts.lost.mc.scriptexecutor.kotlin.interfaces

import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor

interface CommandInitializer {
    fun initialize(plugin: ScriptExecutor)
}