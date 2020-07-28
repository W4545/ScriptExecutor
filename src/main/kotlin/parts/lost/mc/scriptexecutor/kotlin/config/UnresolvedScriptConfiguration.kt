package parts.lost.mc.scriptexecutor.kotlin.config

internal data class UnresolvedScriptConfiguration(
        val name: String,
        var commands: List<String>? = null,
        var workingDirectory: String? = null,
        var wrapOutput: Boolean = false,
        var logFile: Boolean = false,
        var logFileLocation: String? = null
) {
    fun map(): ScriptConfiguration? {
        return commands?.let { ScriptConfiguration(name, it, workingDirectory, wrapOutput, logFile, logFileLocation) }
    }
}