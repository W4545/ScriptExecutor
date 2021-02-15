package parts.lost.mc.scriptexecutor.kotlin.commands

import parts.lost.mc.scriptexecutor.kotlin.commands.commandseautomation.Create
import parts.lost.mc.scriptexecutor.kotlin.constructs.CommandManager
import parts.lost.mc.scriptexecutor.kotlin.constructs.HelpCommand

object CommandSEAutomation : CommandManager() {
    override val helpNotes = HelpCommand(this)
    override val subCommands = listOf(Create, helpNotes)
    override val name = "seautomate"
}