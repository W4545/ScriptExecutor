package parts.lost.mc.scriptexecutor.kotlin.storage

import kotlinx.coroutines.Job

data class RunningScripts(val id: String, val process: Process, val inputJob: Job)