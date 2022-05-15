package dev.jacaro.mc.scriptexecutor.kotlin.storage

import java.util.*

object Storage {
    val runningScripts = mutableSetOf<RunningScript>()

    val automatedScripts = mutableSetOf<AutomatedScript>()

    private val scriptCount = mutableMapOf<String, Int>()

    fun scriptName(name: String): String {
        val count = scriptCount[name] ?: 0
        scriptCount[name] = count + 1
        return "$name-$count"
    }

    fun automatedScriptID(name: String): String {
        val calendar = Calendar.getInstance()
        return "$name-${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.HOUR_OF_DAY)}-${calendar.get(Calendar.MINUTE)}-${calendar.get(Calendar.MILLISECOND)}"
    }
}