package parts.lost.mc.scriptexecutor.kotlin.config

internal data class UnresolvedScriptConfiguration(
        val name: String,
        val scheme: String,
        var commands: List<String>? = null,
        var workingDirectory: String? = null,
        var wrapOutput: Boolean = false,
        var logFile: Boolean = false,
        var logFileLocation: String? = null
) {
    fun map(): ScriptConfiguration? {
        return commands?.let { ScriptConfiguration(name, scheme, it, workingDirectory, wrapOutput, logFile, logFileLocation) }
    }
}