package util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.qameta.allure.Allure

object JsonLoader {
    fun loadJson(fileName: String): String {
        val stream = object {}.javaClass.classLoader
            .getResourceAsStream("$fileName.json")
            ?: error("File $fileName.json not found in resources")
        return stream.bufferedReader().use { it.readText() }
    }
}

object JsonMapper {
    val mapper = jacksonObjectMapper()
    inline fun <reified T> fromJson(json: String): T =
        mapper.readValue(json)
}

object JsonAttachments {
    fun attachJson(json: String, name: String = "Expected JSON") {
        Allure.addAttachment(name, "application/json", json, ".json")
    }
}

object JsonComparator {
    fun getJson(fileName: String): String {
        val json = JsonLoader.loadJson(fileName)
        JsonAttachments.attachJson(json)
        return json
    }

    inline fun <reified T> getObject(fileName: String): T {
        val json = getJson(fileName)
        return JsonMapper.fromJson(json)
    }
}