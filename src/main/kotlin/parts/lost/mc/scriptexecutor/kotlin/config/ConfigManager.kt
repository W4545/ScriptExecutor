package parts.lost.mc.scriptexecutor.kotlin.config

import org.bukkit.configuration.file.FileConfiguration
import java.util.*

object ConfigManager {

    lateinit var config: FileConfiguration

    fun getScripts(): List<Pair<String, String>> = config.getConfigurationSection("scripts")
                ?.getValues(false)?.map {
                Pair(it.key, it.value.toString())
            } ?: Collections.emptyList()
}