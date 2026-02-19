package api.service

import api.client.WeatherApiClient
import io.restassured.response.Response
import models.current.CurrentWeatherResponse

class WeatherApiService {

    private val client = WeatherApiClient()

    fun getWeatherResponse(params: Map<String, Any?>): Response =
        client.getCurrentWeather(params)

    fun getWeatherResponse(city: String): Response =
        getWeatherResponse(
            mapOf(
                "q" to city,
                "aqi" to "no"
            )
        )

    fun getWeatherString(city: String): String =
        getWeatherResponse(city)
            .then()
            .statusCode(200)
            .extract()
            .asPrettyString()

    fun getCurrentWeather(city: String): CurrentWeatherResponse =
        getWeatherResponse(city)
            .then()
            .statusCode(200)
            .extract()
            .`as`(CurrentWeatherResponse::class.java)
}