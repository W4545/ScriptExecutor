package dev.jacaro.mc.scriptexecutor.kotlin.commands.commandscriptexecute

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import dev.jacaro.mc.scriptexecutor.kotlin.ScriptExecutor
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.BasicHelpNotes
import dev.jacaro.mc.scriptexecutor.kotlin.emptyMutableList
import dev.jacaro.mc.scriptexecutor.kotlin.interfaces.SubCommand
import dev.jacaro.mc.scriptexecutor.kotlin.storage.Storage

object Reload: SubCommand {
    override val name = "reload"
    override val helpNotes = BasicHelpNotes(
        this,
        "",
        "Reloads configuration from config file. All scripts must be stopped."
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isNotEmpty())
            sender.sendMessage("${ChatColor.RED}This command does not take any arguments.")
        else if (Storage.runningScripts.isNotEmpty())
            sender.sendMessage("${ChatColor.RED}No scripts can be running to reload config.")
        ScriptExecutor.plugin.reloadConfig()
        sender.sendMessage("${ChatColor.BLUE}Config reloaded.")

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        return emptyMutableList()
    }
}
