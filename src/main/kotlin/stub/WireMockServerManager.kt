package stub


import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.configureFor
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.qameta.allure.Step
import util.ConfigManager

object WireMockServerManager {

    val server: WireMockServer by lazy {
        WireMockServer(
            WireMockConfiguration.options()
                .port(ConfigManager.getInt("wiremock.port"))
        )
    }

    @Step("WireMock stub start")
    fun start() {
        server.start()
        configureFor("localhost", ConfigManager.getInt("wiremock.port"))
        println("WireMock started at port ${ConfigManager.getInt("wiremock.port")}")
    }

    @Step("WireMock stub stop")
    fun stop() {
        server.stop()
        println("WireMock stopped")
    }
}