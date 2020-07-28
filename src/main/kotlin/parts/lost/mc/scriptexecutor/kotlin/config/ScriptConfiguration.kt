package parts.lost.mc.scriptexecutor.kotlin.config

data class ScriptConfiguration(
        val name: String,
        val commands: List<String>,
        val workingDirectory: String?,
        val wrapOutput: Boolean,
        val logFile: Boolean,
        val logFileLocation: String?
) {
    val verbose: String
    get() = "Script:\n" +
            "Name: $name\n" +
            "Commands: ${commands.joinToString(" ")}\n" +
            "Working Directory: $workingDirectory\n" +
            "Wrap Output: $wrapOutput\n" +
            "Generate Log File: $logFile\n" +
            "Log File Location: $logFileLocation"
}