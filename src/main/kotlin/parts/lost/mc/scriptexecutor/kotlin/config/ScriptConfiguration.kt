package parts.lost.mc.scriptexecutor.kotlin.config

data class ScriptConfiguration(val name: String, val commands: List<String>, val workingDirectory: String?, val wrapOutput: Boolean, val logFile: String?)