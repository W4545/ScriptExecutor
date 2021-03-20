package parts.lost.mc.scriptexecutor.kotlin.commands.commandseautomation.create

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import parts.lost.mc.scriptexecutor.kotlin.automation.Parser
import parts.lost.mc.scriptexecutor.kotlin.automation.Scheduler
import parts.lost.mc.scriptexecutor.kotlin.automation.Timing
import parts.lost.mc.scriptexecutor.kotlin.config.ConfigManager
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


object CreateCommandExecutor : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size < 3 || args.size > 5) {
            sender.sendMessage("${ChatColor.RED}Incorrect number of arguments provided")
            return true
        }
        val scriptConfiguration = ConfigManager.getScript(args[0], args[1])

        if (scriptConfiguration == null) {
            sender.sendMessage("${ChatColor.RED} Unable to find or infer script/configuration.")
            return true
        }

        val date = Parser.dateRegex.matchEntire(args[2])?.groupValues
        val delay = Parser.timeLengthRegex.matchEntire(args[2])?.value
        val time = if (delay == null)
            Parser.timeRegex.matchEntire(args[3])?.groupValues
        else
            null
        val period = when {
            time == null && args.size == 4 -> Parser.timeLengthRegex.matchEntire(args[3])?.value
            args.size == 5 -> Parser.timeLengthRegex.matchEntire(args[4])?.value
            else -> null
        }

        if (date != null && time != null) {
            sender.sendMessage(scriptConfiguration.verbose)
            sender.sendMessage("Date: ${date[0]} Time: ${time[0]}")
            val zoneID = if (ConfigManager.timeZoneOverride !== null) {
                val zone = TimeZone.getTimeZone(ConfigManager.timeZoneOverride).toZoneId()
                sender.sendMessage("Timezone override detected, using time zone ${zone.id}")
                zone
            }
            else {
                sender.sendMessage("Detected timezone: ${TimeZone.getDefault().displayName}")
                ZoneId.systemDefault()
            }

            val rawTime = Date.from(
                LocalDateTime.of(
                date[1].toInt(),
                date[2].toInt(),
                date[3].toInt(),
                time[1].toInt(),
                time[2].toInt()
            ).atZone(zoneID).toInstant())

            sender.sendMessage(rawTime.toString())
            val automatedScript = if (period != null) {
                sender.sendMessage("Interval: $period")
                scriptConfiguration.additionalConfigurations["timing"] = Timing(date = rawTime, period = period.toString())
                val rawPeriod = Parser.parseTimeLength(period)
                Scheduler.schedule(scriptConfiguration, rawTime, rawPeriod)
            } else {
                scriptConfiguration.additionalConfigurations["timing"] = Timing(date = rawTime)
                Scheduler.schedule(scriptConfiguration, rawTime)
            }

            sender.sendMessage("${ChatColor.GREEN}A automated script was created with ID \"${automatedScript.scriptID}\"")
        } else if (delay != null) {
            sender.sendMessage(scriptConfiguration.verbose)
            sender.sendMessage("Delay: $delay")
            val rawDelay = Parser.parseTimeLength(delay)
            scriptConfiguration.additionalConfigurations["automatedDelay"] = delay
            val automatedScript = if (period != null) {
                sender.sendMessage("Interval: $period")
                val rawPeriod = Parser.parseTimeLength(period)
                scriptConfiguration.additionalConfigurations["timing"] = Timing(delay, period)
                Scheduler.schedule(scriptConfiguration, rawDelay, rawPeriod)
            } else {
                scriptConfiguration.additionalConfigurations["timing"] = Timing(delay)
                Scheduler.schedule(scriptConfiguration, rawDelay)
            }
            sender.sendMessage("${ChatColor.GREEN}A automated script was created with ID \"${automatedScript.scriptID}\"")
        } else
            sender.sendMessage("${ChatColor.RED}Unknown arguments provided. " +
                    "See ${ChatColor.DARK_RED}/$label help create${ChatColor.RESET}${ChatColor.RED} for details")

        return true
    }
}