package parts.lost.mc.scriptexecutor.kotlin.automation

import org.bukkit.configuration.file.FileConfiguration
import java.time.Instant
import java.util.Date


interface Timing {
    val period: Long
    val delay: Long
}

class DateTiming(val date: Date, override val period: Long = 0) : Timing {
    override val delay: Long
        get() = date.time - Date.from(Instant.now()).time
}

class BasicTiming(override val period: Long, override val delay: Long) : Timing

object Timings {
    fun factory(file: FileConfiguration): Timing {
        val date = file.getString("date")
        if (date != null)  {

        }

        TODO()
    }
}