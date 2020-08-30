package parts.lost.mc.scriptexecutor.kotlin.storage

object Storage {
    val runningScripts = mutableSetOf<RunningScript>()

    val automatedScripts = mutableSetOf<AutomatedScript>()

    private val scriptCount = mutableMapOf<String, Int>()

    fun scriptName(name: String): String {
        val count = scriptCount[name] ?: 0
        scriptCount[name] = count + 1
        return "$name-$count"
    }
}