package stub

import com.github.tomakehurst.wiremock.client.WireMock.*
import util.ConfigManager
import java.nio.file.Files
import java.nio.file.Paths

object DynamicStubs {

    private val apiKey = ConfigManager.get("API_KEY")

    fun stub(
        queryParams: Map<String, String>,
        responseFile: String,
        status: Int
    ) {
        val jsonBody = Files.readString(Paths.get(responseFile))
        val mappingBuilder = get(urlPathEqualTo("/current.json"))

        queryParams.forEach { (key, value) ->
            mappingBuilder.withQueryParam(key, equalTo(value))
        }

        WireMockServerManager.server.stubFor(
            mappingBuilder.willReturn(
                aResponse()
                    .withStatus(status)
                    .withHeader("Content-Type", "application/json")
                    .withBody(jsonBody)
            )
        )
    }

    fun stubPositive(city: String, responseFile: String) {
        val param = mapOf(
            "key" to apiKey,
            "q" to city,
            "aqi" to "no"
        )
        stub(queryParams = param, responseFile = responseFile, status = 200)
    }

    fun stubError(
        responseFile: String,
        status: Int,
        queryParams: Map<String, String>
    ) {
        stub(queryParams, responseFile, status)
    }
}