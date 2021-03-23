package dev.jacaro.mc.scriptexecutor.kotlin.interfaces

import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter


interface SubCommand : CommandExecutor, TabCompleter {
    val name: String
    val helpNotes: HelpNotes
}