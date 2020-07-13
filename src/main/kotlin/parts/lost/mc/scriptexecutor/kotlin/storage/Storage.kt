package parts.lost.mc.scriptexecutor.kotlin.storage

object Storage {
    val runningScripts = mutableSetOf<RunningScript>()

    private val scriptCount = mutableMapOf<String, Int>()

    fun scriptName(name: String): String {
        val count = scriptCount[name] ?: 0
        scriptCount[name] = count + 1
        return "$name-$count"
    }
}