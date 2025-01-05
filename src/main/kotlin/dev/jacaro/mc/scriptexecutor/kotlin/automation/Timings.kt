/**
 * Copyright (C) 2025 Jack Young
 * This file is part of ScriptExecutor <https://github.com/W4545/ScriptExecutor>.
 *
 * ScriptExecutor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ScriptExecutor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ScriptExecutor.  If not, see <http://www.gnu.org/licenses/>.
 */
package dev.jacaro.mc.scriptexecutor.kotlin.automation

import org.bukkit.configuration.ConfigurationSection
import dev.jacaro.mc.scriptexecutor.kotlin.config.ConfigManager
import java.text.SimpleDateFormat
import java.util.*



data class Timing(val delay: String? = null, val period: String? = null, val date: Date? = null)

object Timings {

    val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

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
            file["delay"] = timing.delay ?: "0s"
        else
            file["date"] = dateFormatter.format(timing.date)

        if (timing.period != null)
            file["period"] = timing.period
    }
}