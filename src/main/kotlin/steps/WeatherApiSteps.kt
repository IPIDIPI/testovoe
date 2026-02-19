package steps

import api.service.WeatherApiService
import io.qameta.allure.Step
import models.current.CurrentWeatherResponse
import models.current.ErrorResponse
import net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import util.JsonComparator
import util.RecursiveAssertHelper


class WeatherApiSteps {

    private val api = WeatherApiService()

    /*
    * тут на шажочки поделил для спока
    * */
    //given
    @Step("Expected json weather for city: {city}")
    fun getExpectedWeather(city: String): CurrentWeatherResponse {
        val resource = object {}.javaClass.classLoader
            .getResource("${city.lowercase()}_expected")
        val file = if (resource != null) "${city.lowercase()}_expected" else city.lowercase()
        return JsonComparator.getObject(file)
    }

    //when
    @Step("Request current weather for city: {city}")
    fun getActualWeather(city: String): CurrentWeatherResponse {
        return api.getCurrentWeather(city)
    }

    //than
    @Step("Response should match expected weather")
    fun compareWeather(
        actual: CurrentWeatherResponse,
        expected: CurrentWeatherResponse
    ): Boolean {
        assertThat(actual).isEqualTo(expected)
        return true
    }


    @Step("Verify error response for parameters: {queryParams}")
    fun verifyErrorWeather(queryParams: Map<String, Any?>, caseName: String) {
        val (errorCode, expectedFile) = when (caseName) {
            "Param missing" -> 400 to "1003"
            "No match loc" -> 400 to "1006"
            "Invalid key" -> 401 to "2006"
            "Quota exceeded" -> 403 to "2007"
            else -> throw IllegalArgumentException("Нет такого кейса")
        }
        val actual = api.getWeatherResponse(queryParams)
            .then()
            .statusCode(errorCode)
            .extract()
            .`as`(ErrorResponse::class.java)

        val expected = JsonComparator.getObject<ErrorResponse>(expectedFile)
        assertThat(actual).isEqualTo(expected)
    }

    @Step("Verify current weather from file for city: {city}")
    fun verifyCurrentDifferentWeather(city: String, expectedCity: String): Boolean {
        val softly = SoftAssertions()
        runCatching { compareDto(city, expectedCity) }
            .onFailure { softly.fail("DTO comparison failed: ${it.message}") }
        runCatching { compareJson(city, expectedCity) }
            .onFailure { softly.fail("JSON comparison failed: ${it.message}") }
        softly.assertAll()
        return true
    }

    @Step("compareJson current weather")
    fun compareDto(city: String, expectedCity: String) {
        val actual = api.getCurrentWeather(city)
        val expected = JsonComparator.getObject<CurrentWeatherResponse>(expectedCity)
        RecursiveAssertHelper.assertEqual(actual, expected)
    }

    @Step("compareJson current weather")
    fun compareJson(city: String, expectedCity: String) {
        val actualJson = api.getWeatherString(city)
        val expectedJson = JsonComparator.getJson(expectedCity)
        assertThatJson(actualJson)
            .isEqualTo(expectedJson)
    }
}


