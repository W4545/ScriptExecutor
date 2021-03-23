package dev.jacaro.mc.scriptexecutor.kotlin.storage

import org.bukkit.scheduler.BukkitTask
import dev.jacaro.mc.scriptexecutor.kotlin.config.ScriptConfiguration

data class AutomatedScript(val scriptID: String, val scriptConfiguration: ScriptConfiguration, var bukkitTask: BukkitTask?, val deleteOnCompletion: Boolean)