package parts.lost.mc.scriptexecutor.kotlin.automation

import org.bukkit.configuration.ConfigurationSection
import parts.lost.mc.scriptexecutor.kotlin.config.ConfigManager
import java.text.SimpleDateFormat
import java.util.*



data class Timing(val delay: String? = null, val period: String? = null, val date: Date? = null)

object Timings {

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

    init {
        dateFormatter.timeZone = if (ConfigManager.timeZoneOverride == null)
            TimeZone.getDefault()
        else
            TimeZone.getTimeZone(ConfigManager.timeZoneOverride)
    }

    fun factory(file: ConfigurationSection): Timing {
        return if (file.contains("date"))  {
            val rawDate = file.getString("date")
            val period = file.getString("period")

            val date = dateFormatter.parse(rawDate)

            Timing(period = period, date = date)
        } else {
            val delay = file.getString("delay")
            val period = file.getString("period")

            Timing(delay, period)
        }
    }

    fun write(file: ConfigurationSection, timing: Timing) {
        if (timing.date == null)
            file["delay"] = timing.delay.toString()
        else
            file["date"] = dateFormatter.format(timing.date)

        file["period"] = timing.period.toString()
    }
}