package parts.lost.mc.scriptexecutor.kotlin

import parts.lost.mc.scriptexecutor.kotlin.commands.CommandScriptExecute

object Commands {
    fun initCommands(plugin: ScriptExecutor) {
        CommandScriptExecute.initialize(plugin)
    }
}