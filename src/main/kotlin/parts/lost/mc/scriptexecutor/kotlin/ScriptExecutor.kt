package parts.lost.mc.scriptexecutor.kotlin

import org.bukkit.plugin.java.JavaPlugin
import parts.lost.mc.scriptexecutor.kotlin.commands.CommandScriptExecute
import java.util.logging.Level

class ScriptExecutor : JavaPlugin() {

    override fun onEnable() {
        plugin = this
        this.saveDefaultConfig()

        CommandScriptExecute.initialize()

        logger.log(Level.INFO, "ScriptExecutor Enabled")
    }

    override fun onDisable() {
        logger.log(Level.INFO, "ScriptExecutor Disabled")
    }

    companion object {
        lateinit var plugin: ScriptExecutor
        private set
    }
}
