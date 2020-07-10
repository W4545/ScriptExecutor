package parts.lost.mc.scriptexecutor.kotlin

import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

class ScriptExecutor : JavaPlugin() {
    override fun onEnable() {
        for (commandTemplate in Commands.commands()) {
            val pluginCommand = getCommand(commandTemplate.name)!!

            pluginCommand.setExecutor(commandTemplate)
            pluginCommand.tabCompleter = commandTemplate
        }

        logger.log(Level.INFO, "ScriptExecutor Enabled")
    }

    override fun onDisable() {
        logger.log(Level.INFO, "Disabling ScriptExecutor")
    }
}