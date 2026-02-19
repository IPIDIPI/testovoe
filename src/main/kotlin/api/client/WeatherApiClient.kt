package api.client

import io.qameta.allure.Step
import io.restassured.RestAssured
import io.restassured.response.Response
import util.ConfigManager

class WeatherApiClient : InitApiClient() {

    private val defaultApiKey = ConfigManager.get("API_KEY")
    private val baseUrl = ConfigManager.get("BASE_URL")

    private fun request() =
        RestAssured.given()
            .baseUri(baseUrl)
            .contentType("application/json")
            .accept("application/json")

    @Step("GET /api/current")
    fun getCurrentWeather(
        queryParams: Map<String, Any?> = emptyMap()
    ): Response {
        val params = mutableMapOf<String, Any?>(
            "key" to defaultApiKey
        )
        params.putAll(queryParams)
        return request()
            .queryParams(params.filterValues { it != null })
            .get("/current.json")
    }
}

