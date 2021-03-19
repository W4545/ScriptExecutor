package parts.lost.mc.scriptexecutor.kotlin.automation.config.versions

import parts.lost.mc.scriptexecutor.kotlin.ScriptExecutor
import parts.lost.mc.scriptexecutor.kotlin.automation.Parser
import parts.lost.mc.scriptexecutor.kotlin.automation.Scheduler
import parts.lost.mc.scriptexecutor.kotlin.config.UnresolvedScriptConfiguration
import parts.lost.mc.scriptexecutor.kotlin.config.versions.one.ConfigVersion1
import parts.lost.mc.scriptexecutor.kotlin.exceptions.ScriptExecutorConfigException
import parts.lost.mc.scriptexecutor.kotlin.storage.AutomatedScript
import java.lang.RuntimeException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Level


object AutomationConfig1 : AutomationConfig {
    override val version: Int
        get() = ScriptExecutor.plugin.automationConfig.getInt("version")

    override val automatedScriptNames: List<String>
        get() = ScriptExecutor.plugin.automationConfig.getConfigurationSection("automated-scripts")?.getKeys(false)?.toList() ?: emptyList()


    override fun loadScript(string: String): AutomatedScript {
        val fileSection = ScriptExecutor.plugin.automationConfig.getConfigurationSection(string)
            ?: throw RuntimeException("Script doesn't exist")

        val unresolvedScriptConfiguration = UnresolvedScriptConfiguration("automated", "automated")

        ConfigVersion1.loadValues(fileSection.getConfigurationSection("configuration"), unresolvedScriptConfiguration)

        val configuration =
            unresolvedScriptConfiguration.map() ?: throw ScriptExecutorConfigException("automated script")

        val scriptID = fileSection.getString("scriptID") ?: throw ScriptExecutorConfigException("automated script")

        val deleteOnComplete = fileSection.getBoolean("deleteOnComplete")

//        val date = fileSection.getString("date")?.let { rawDate ->
//            fileSection.getString("time")?.let { rawTime ->
//                Parser.parseDateTime(rawDate, rawTime)
//            }
//        }

        val date = fileSection.getString("date")?.let {
            if (it.all { char -> char.isDigit() })
                Date(it.toLong())
            else
                throw RuntimeException("Error parsing date")
        }

        val delay = fileSection.getString("delay")?.let {
            Parser.parseTimeLength(it)
        }

        val period = fileSection.getString("period")?.let {
            Parser.parseTimeLength(it)
        } ?: 0L


        return when {
            date != null -> Scheduler.schedule(configuration, date, period)
            delay != null -> Scheduler.schedule(configuration, delay, period)
            else -> throw RuntimeException("Error Parsing automation")
        }
    }

    override fun writeScript(automatedScript: AutomatedScript) {
        val automatedSection = ScriptExecutor.plugin.automationConfig.getConfigurationSection("automatedScripts")?.createSection(automatedScript.scriptID) ?: ScriptExecutor.plugin.automationConfig.createSection("automatedScripts").createSection(automatedScript.scriptID)

        automatedScript.scriptConfiguration.additionalConfigurations["automatedDate"]?.let {
            automatedSection.set("date", it)
        }

        automatedScript.scriptConfiguration.additionalConfigurations["automatedDelay"]?.let {
            automatedSection.set("delay", it)
        }

        automatedScript.scriptConfiguration.additionalConfigurations["automatedPeriod"]?.let {
            automatedSection.set("period", it)
        }

        val configuration = automatedSection.createSection("configuration")
        configuration.set("commands", automatedScript.scriptConfiguration.commands)
        configuration.set("workingDirectory", automatedScript.scriptConfiguration.workingDirectory)
        configuration.set("wrapOutput", automatedScript.scriptConfiguration.wrapOutput)
        configuration.set("logFile", automatedScript.scriptConfiguration.logFile)
        configuration.set("logFileLocation", automatedScript.scriptConfiguration.logFileLocation)

        ScriptExecutor.plugin.automationConfig.save(ScriptExecutor.plugin.automationFile)
    }


}