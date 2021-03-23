package dev.jacaro.mc.scriptexecutor.kotlin.exceptions

import java.lang.RuntimeException

class ScriptCancelException : RuntimeException("Couldn't cancel script")