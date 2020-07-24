package parts.lost.mc.scriptexecutor.kotlin.exceptions

import java.lang.RuntimeException

class ScriptExecutorConfigException(script: String): RuntimeException("An error occurred when parsing the config file on script $script")
