package parts.lost.mc.scriptexecutor.kotlin.automation

import parts.lost.mc.scriptexecutor.kotlin.config.ConfigManager
import java.lang.RuntimeException
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


object Parser {

    val dateRegex = """(\d{4})-(\d{1,2})-(\d{1,2})""".toRegex()
    val timeRegex = """(\d{1,2}):(\d{2})""".toRegex()
    val timeLengthRegex = """\d+[smhd]""".toRegex()

    fun parseTimeLength(length: String?): Long = when {
        length == null -> 0L
        length.endsWith('s') -> length.trim { !it.isDigit() }.toLong() * 20
        length.endsWith('m') -> length.trim { !it.isDigit() }.toLong() * 1200
        length.endsWith('h') -> length.trim { !it.isDigit() }.toLong() * 72000
        length.endsWith('d') -> length.trim { !it.isDigit() }.toLong() * 1728000
        else -> throw RuntimeException("An unknown error occurred parsing delay")
    }

    fun parseDateTime(date: String, time: String): Date {
        val dateResult = dateRegex.matchEntire(date)?.groupValues ?: throw RuntimeException("Date Match error.")

        val timeResult = timeRegex.matchEntire(time)?.groupValues ?: throw RuntimeException("Time match error.")

        val zoneID = if (ConfigManager.timeZoneOverride != null)
            TimeZone.getTimeZone(ConfigManager.timeZoneOverride).toZoneId()
        else
            ZoneId.systemDefault()


        return Date.from(
            LocalDateTime.of(
                dateResult[1].toInt(),
                dateResult[2].toInt(),
                dateResult[3].toInt(),
                timeResult[1].toInt(),
                timeResult[2].toInt()
            ).atZone(zoneID).toInstant()
        )
    }

}