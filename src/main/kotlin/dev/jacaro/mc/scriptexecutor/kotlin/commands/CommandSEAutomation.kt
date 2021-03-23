package dev.jacaro.mc.scriptexecutor.kotlin.commands

import dev.jacaro.mc.scriptexecutor.kotlin.commands.commandseautomation.CancelAutomation
import dev.jacaro.mc.scriptexecutor.kotlin.commands.commandseautomation.AutomatedScriptList
import dev.jacaro.mc.scriptexecutor.kotlin.commands.commandseautomation.DebugAutomation
import dev.jacaro.mc.scriptexecutor.kotlin.commands.commandseautomation.create.Create
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.CommandManager
import dev.jacaro.mc.scriptexecutor.kotlin.constructs.HelpCommand

object CommandSEAutomation : CommandManager() {
    override val helpNotes = HelpCommand(this)
    override val subCommands = listOf(Create, helpNotes, AutomatedScriptList, CancelAutomation, DebugAutomation)
    override val name = "seautomate"
}