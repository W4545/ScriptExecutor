package dev.jacaro.mc.scriptexecutor.kotlin.automation.config.versions

import dev.jacaro.mc.scriptexecutor.kotlin.ScriptExecutor
import dev.jacaro.mc.scriptexecutor.kotlin.automation.Parser
import dev.jacaro.mc.scriptexecutor.kotlin.automation.Scheduler
import dev.jacaro.mc.scriptexecutor.kotlin.automation.Timing
import dev.jacaro.mc.scriptexecutor.kotlin.automation.Timings
import dev.jacaro.mc.scriptexecutor.kotlin.config.UnresolvedScriptConfiguration
import dev.jacaro.mc.scriptexecutor.kotlin.config.versions.one.ConfigVersion1
import dev.jacaro.mc.scriptexecutor.kotlin.exceptions.ScriptExecutorConfigException
import dev.jacaro.mc.scriptexecutor.kotlin.storage.AutomatedScript
import java.lang.RuntimeException


object AutomationConfig1 : AutomationConfig {
    override val version: Int
        get() = ScriptExecutor.plugin.automationConfig.getInt("version")

    override val automatedScriptNames: List<String>
        get() = ScriptExecutor.plugin.automationConfig.getConfigurationSection("automatedScripts")?.getKeys(false)?.toList() ?: emptyList()


    override fun loadScript(string: String): AutomatedScript {
        val fileSection = ScriptExecutor.plugin.automationConfig.getConfigurationSection("automatedScripts")?.getConfigurationSection(string)
            ?: throw RuntimeException("Script doesn't exist")

        val unresolvedScriptConfiguration = UnresolvedScriptConfiguration("automated", "generated")

        ConfigVersion1.loadValues(fileSection.getConfigurationSection("configuration"), unresolvedScriptConfiguration)

        val configuration =
            unresolvedScriptConfiguration.map() ?: throw ScriptExecutorConfigException("automated script")

        val timing = Timings.factory(fileSection)
        configuration.additionalConfigurations["timing"] = timing
        configuration.additionalConfigurations["automatedScriptID"] = string

        return if (timing.period == null) {
            if (timing.date == null)
                Scheduler.schedule(configuration, Parser.parseTimeLength(timing.delay))
            else
                Scheduler.schedule(configuration, timing.date)
        } else {
            if (timing.date == null)
                Scheduler.schedule(configuration, Parser.parseTimeLength(timing.delay), Parser.parseTimeLength(timing.period))
            else
                Scheduler.schedule(configuration, timing.date, Parser.parseTimeLength(timing.period))
        }
    }

    override fun writeScript(automatedScript: AutomatedScript) {
        if (ScriptExecutor.plugin.automationConfig.getConfigurationSection("automatedScript")?.isConfigurationSection(automatedScript.scriptID) == true)
            return
        val automatedSection = ScriptExecutor.plugin.automationConfig.getConfigurationSection("automatedScripts")?.createSection(automatedScript.scriptID) ?: ScriptExecutor.plugin.automationConfig.createSection("automatedScripts").createSection(automatedScript.scriptID)

        Timings.write(automatedSection, automatedScript.scriptConfiguration.additionalConfigurations["timing"] as Timing)

        val configuration = automatedSection.createSection("configuration")
        configuration["commands"] = automatedScript.scriptConfiguration.commands
        configuration["workingDirectory"] = automatedScript.scriptConfiguration.workingDirectory
        configuration["wrapOutput"] = automatedScript.scriptConfiguration.wrapOutput
        configuration["logFile"] = automatedScript.scriptConfiguration.logFile
        configuration["logFileLocation"] = automatedScript.scriptConfiguration.logFileLocation

        ScriptExecutor.plugin.automationConfig.save(ScriptExecutor.plugin.automationFile)
    }

    override fun deleteScript(string: String) {
        ScriptExecutor.plugin.automationConfig.set("automatedScripts.$string", null)
        ScriptExecutor.plugin.automationConfig.save(ScriptExecutor.plugin.automationFile)
    }
}