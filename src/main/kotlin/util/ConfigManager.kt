package util

import java.util.*

object ConfigManager {
    private val props: Properties = Properties().apply {
        load(ConfigManager::class.java.classLoader.getResourceAsStream("config/env.properties"))
    }

    fun get(key: String): String = props.getProperty(key)
    fun getInt(key: String): Int = props.getProperty(key).toInt()
}