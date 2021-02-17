package parts.lost.mc.scriptexecutor.kotlin.config

data class ScriptConfiguration(
        val name: String,
        val scheme: String,
        val commands: List<String>,
        val workingDirectory: String?,
        val wrapOutput: Boolean,
        val logFile: Boolean,
        val logFileLocation: String?,
        val additionalConfigurations: MutableMap<String, String> = mutableMapOf()
) {
    val verbose: String
    get() = "Name: $name\n" +
            "  Scheme: $scheme\n" +
            "  Commands: ${commands.joinToString(" ")}\n" +
            "  Working Directory: $workingDirectory\n" +
            "  Wrap Output: $wrapOutput\n" +
            "  Generate Log File: $logFile\n" +
            "  Log File Location: $logFileLocation"
}