package parts.lost.mc.scriptexecutor.kotlin.storage

import org.bukkit.scheduler.BukkitTask
import parts.lost.mc.scriptexecutor.kotlin.config.ScriptConfiguration

data class AutomatedScript(val scriptID: String, val scriptConfiguration: ScriptConfiguration, val bukkitTask: BukkitTask)