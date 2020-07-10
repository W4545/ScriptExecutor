package parts.lost.mc.scriptexecutor.kotlin

import parts.lost.mc.scriptexecutor.kotlin.commands.CommandScriptExecute
import parts.lost.mc.scriptexecutor.kotlin.commands.SECommand

object Commands {

    fun commands(): List<SECommand> {
        return listOf<SECommand>(
            CommandScriptExecute()
        )
    }
}