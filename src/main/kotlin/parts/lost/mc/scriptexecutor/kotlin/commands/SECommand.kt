package parts.lost.mc.scriptexecutor.kotlin.commands

import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter

abstract class SECommand(val name: String) : CommandExecutor, TabCompleter