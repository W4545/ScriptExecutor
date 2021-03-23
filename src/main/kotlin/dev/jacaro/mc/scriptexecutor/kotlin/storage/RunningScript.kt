package dev.jacaro.mc.scriptexecutor.kotlin.storage

import kotlinx.coroutines.Job
import dev.jacaro.mc.scriptexecutor.kotlin.config.ScriptConfiguration

data class RunningScript(
    val id: String,
    val process: Process,
    val inputJob: Job?,
    val scriptConfiguration: ScriptConfiguration,
    var isAliveJob: Job? = null,
    val automatedScript: AutomatedScript? = null) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RunningScript

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}