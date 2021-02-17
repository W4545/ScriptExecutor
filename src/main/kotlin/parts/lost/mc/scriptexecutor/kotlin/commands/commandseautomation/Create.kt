package parts.lost.mc.scriptexecutor.kotlin.commands.commandseautomation

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import parts.lost.mc.scriptexecutor.kotlin.automation.Scheduler
import parts.lost.mc.scriptexecutor.kotlin.config.ConfigManager
import parts.lost.mc.scriptexecutor.kotlin.endsWith
import parts.lost.mc.scriptexecutor.kotlin.interfaces.HelpNotes
import parts.lost.mc.scriptexecutor.kotlin.interfaces.SubCommand
import java.lang.RuntimeException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

private fun parseTimeLength(length: String): Long = when {
    length.endsWith('s') -> length.trim { !it.isDigit() }.toLong() * 20
    length.endsWith('m') -> length.trim { !it.isDigit() }.toLong() * 1200
    length.endsWith('h') -> length.trim { !it.isDigit() }.toLong() * 72000
    length.endsWith('d') -> length.trim { !it.isDigit() }.toLong() * 1728000
    else -> throw RuntimeException("An unknown error occurred parsing delay")
}

object Create: SubCommand {
    private val digits = arrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '0')

    private val dateRegex = """(\d{4})-(\d{1,2})-(\d{1,2})""".toRegex()
    private val timeRegex = """(\d{1,2}):(\d{2})""".toRegex()
    private val timeLengthRegex = """\d+[smhd]""".toRegex()

    override val name = "create"
    override val helpNotes = object : HelpNotes {
        override fun onCommand(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<out String>
        ): Boolean {
            sender.sendMessage("Description: " +
                    "Creates a new automation request with the given delay or datetime, and optional period")
            sender.sendMessage("Examples: /$label create hello console 1m 10s " +
                    "(create an automation script that runs every 10 seconds starting in 1 minute.)")
            sender.sendMessage("Examples: /$label create hello default 2021-12-1 14:30 " +
                    "(creates an automation script that runs on the date and time provided)")
            sender.sendMessage("Usage: /$label create <script> <script configuration> <delay or date> <optional time> <optional period>")

            return true
        }

    }

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

        val date = dateRegex.matchEntire(args[2])?.groupValues
        val delay = timeLengthRegex.matchEntire(args[2])?.value
        val time = if (delay == null)
            timeRegex.matchEntire(args[3])?.groupValues
        else
            null
        val period = when {
            time == null && args.size == 4 -> timeLengthRegex.matchEntire(args[3])?.value
            args.size == 5 -> timeLengthRegex.matchEntire(args[4])?.value
            else -> null
        }

        if (date != null && time != null) {
            sender.sendMessage(scriptConfiguration.verbose)
            sender.sendMessage("Date: ${date[0]} Time: ${time[0]}")
            val year = date[1].toInt()
            val month = date[2].toInt()
            val day = date[3].toInt()
            val hour = time[1].toInt()
            val minute = time[2].toInt()
            val zoneID = if (ConfigManager.timeZoneOverride !== null) {
                val zone = TimeZone.getTimeZone(ConfigManager.timeZoneOverride).toZoneId()
                sender.sendMessage("Timezone override detected, using time zone ${zone.id}")
                zone
            }
            else {
                sender.sendMessage("Detected timezone: ${TimeZone.getDefault().displayName}")
                ZoneId.systemDefault()
            }

            val rawTime = Date.from(LocalDateTime.of(year, month, day, hour, minute).atZone(zoneID).toInstant())
            sender.sendMessage(rawTime.toString())
            val automatedScript = if (period != null) {
                sender.sendMessage("Interval: $period")

                val rawPeriod = parseTimeLength(period)
                Scheduler.schedule(scriptConfiguration, rawTime, rawPeriod)
            } else {
                Scheduler.schedule(scriptConfiguration, rawTime)
            }

            sender.sendMessage("${ChatColor.GREEN}A automated script was created with ID \"${automatedScript.scriptID}\"")
        } else if (delay != null) {
            sender.sendMessage(scriptConfiguration.verbose)
            sender.sendMessage("Delay: $delay")
            val rawDelay = parseTimeLength(delay)

            val automatedScript = if (period != null) {
                sender.sendMessage("Interval: $period")
                val rawPeriod = parseTimeLength(period)

                Scheduler.schedule(scriptConfiguration, rawDelay, rawPeriod)
            } else
                Scheduler.schedule(scriptConfiguration, rawDelay)
            sender.sendMessage("${ChatColor.GREEN}A automated script was created with ID \"${automatedScript.scriptID}\"")
        } else
            sender.sendMessage("${ChatColor.RED}Unknown arguments provided. " +
                    "See ${ChatColor.DARK_RED}/$label help create${ChatColor.RESET}${ChatColor.RED} for details")

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        return when (args.size) {
            2 -> ConfigManager.getScriptNames().toMutableList()
            3 -> ConfigManager.getScriptSchemeConfigurations(args[1]).toMutableList()
            4 -> {
                if (args[3].endsWith(digits)) {
                    if (args[3].contains('-')) {
                        if (args[3].count { it == '-' } < 2)
                            mutableListOf(args[3] + "-")
                        else
                            MutableList(0) { "" }
                    } else {
                        if (args[3].all { it.isDigit() } && args[3].length == 4)
                            arrayOf("-", "s", "m", "h", "d").map { args[3] + it }.toMutableList()
                        else
                            arrayOf("s", "m", "h", "d").map { args[3] + it }.toMutableList()
                    }
                }
                else MutableList(0) { "" }
            }
            5 -> {
                if (args[4].endsWith(digits) && args[4].count { it == ':' } == 0)
                    arrayOf("-", "s", "m", "h", "d", ":").map { args[4] + it }.toMutableList()
                else
                    MutableList(0) { "" }
            }
            6 -> {
                if (args[5].endsWith(digits))
                    arrayOf("s", "m", "h", "d").map { args[5] + it }.toMutableList()
                else
                    MutableList(0) { "" }
            }
            else -> MutableList(0) { "" }
        }
    }
}