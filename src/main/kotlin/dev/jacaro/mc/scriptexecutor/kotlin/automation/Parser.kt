/**
 * Copyright (C) 2022 Jack Young
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

import dev.jacaro.mc.scriptexecutor.kotlin.config.ConfigManager
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