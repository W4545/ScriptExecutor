package parts.lost.mc.scriptexecutor.kotlin

import org.bukkit.plugin.java.JavaPlugin
import parts.lost.mc.scriptexecutor.kotlin.config.ConfigManager
import java.util.logging.Level

class ScriptExecutor : JavaPlugin() {

    override fun onEnable() {
        this.saveDefaultConfig()
        ConfigManager.plugin = this

        Commands.initCommands(this)

        logger.log(Level.INFO, "ScriptExecutor Enabled")
    }

    override fun onDisable() {
        logger.log(Level.INFO, "ScriptExecutor Disabled")
    }
}