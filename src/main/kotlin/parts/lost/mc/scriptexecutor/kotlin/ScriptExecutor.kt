package parts.lost.mc.scriptexecutor.kotlin

import org.bukkit.command.PluginCommand
import org.bukkit.plugin.java.JavaPlugin
import parts.lost.mc.scriptexecutor.kotlin.commands.CommandScriptExecute
import parts.lost.mc.scriptexecutor.kotlin.commands.SECommand
import java.util.logging.Level

class ScriptExecutor : JavaPlugin() {
    override fun onEnable() {
        val command: PluginCommand = getCommand("scriptexecute")!!
        val commandScriptExecute: SECommand = CommandScriptExecute()
        command.setExecutor(commandScriptExecute)
        command.tabCompleter = commandScriptExecute

        logger.log(Level.INFO, "ScriptExecutor Enabled")
    }

    override fun onDisable() {
        logger.log(Level.INFO, "Disabling ScriptExecutor")
    }
}