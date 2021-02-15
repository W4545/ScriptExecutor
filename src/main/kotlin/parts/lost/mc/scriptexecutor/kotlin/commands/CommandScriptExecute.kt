package parts.lost.mc.scriptexecutor.kotlin.commands


import org.bukkit.ChatColor
import org.bukkit.command.*
import org.bukkit.entity.Player
import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
import parts.lost.mc.scriptexecutor.kotlin.commands.commandscriptexecute.*
import parts.lost.mc.scriptexecutor.kotlin.config.ConfigManager
import parts.lost.mc.scriptexecutor.kotlin.constructs.CommandManager
import parts.lost.mc.scriptexecutor.kotlin.constructs.HelpCommand
import parts.lost.mc.scriptexecutor.kotlin.interfaces.CommandInitializer
import parts.lost.mc.scriptexecutor.kotlin.interfaces.HelpNotes
import parts.lost.mc.scriptexecutor.kotlin.storage.Storage
import java.util.Collections

object CommandScriptExecute : CommandManager() {

    override val name = "scriptexecute"
    override val helpNotes = HelpCommand(this)
    override val subCommands = listOf(Exec, helpNotes, Cancel, Reload, Running, SubCommandList, TestConfiguration)


    fun infer(sender: CommandSender, args: Array<out String>): String {
        return if (sender is Player && ConfigManager.getScriptSchemeConfigurations(args[0]).contains("player"))
            "player"
        else if (sender is ConsoleCommandSender && ConfigManager.getScriptSchemeConfigurations(args[0])
                .contains("console")
        )
            "console"
        else
            "default"
    }

}
