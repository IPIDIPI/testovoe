import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Title
import spock.lang.Unroll
import steps.WeatherApiSteps
import stub.DynamicStubs
import stub.WireMockServerManager
import util.ConfigManager

@Title("Negative Weather Tests")
class NegativeCurrentWeatherSpec extends Specification {

    WeatherApiSteps steps = new WeatherApiSteps()
    @Shared
    def basePath = "src/test/resources/"
    @Shared
    def validApiKey = ConfigManager.INSTANCE.get("API_KEY")
    @Shared
    def invalidApiKey = ConfigManager.INSTANCE.get("INVALID_API_KEY")

    def setupSpec() {
        def stubs = DynamicStubs.INSTANCE
        WireMockServerManager.INSTANCE.start()
        stubs.stubError("${basePath}1003.json", 400, ["q": "", "aqi": "no", "key": validApiKey])
        stubs.stubError("${basePath}1006.json", 400, ["q": "asdfqwe", "aqi": "no", "key": validApiKey])
        stubs.stubError("${basePath}2006.json", 401, ["q": "Minsk", "aqi": "no", "key": invalidApiKey])
        stubs.stubError("${basePath}2007.json", 403, ["q": "Kaliningrad", "aqi": "no", "key": validApiKey])

        stubs.stubPositive("Asdf", "${basePath}asdf.json")
        println "WireMock stub start"
    }

    def cleanupSpec() {
        WireMockServerManager.INSTANCE.stop()
        println "WireMock stub stop"
    }

    private Map buildParams(String caseName) {
        switch (caseName) {
            case "Param missing":
                return ["q": "", "aqi": "no", "key": validApiKey]
            case "No match loc":
                return ["q": "asdfqwe", "aqi": "no", "key": validApiKey]
            case "Invalid key":
                return ["q": "Minsk", "aqi": "no", "key": invalidApiKey]
            case "Quota exceeded":
                return ["q": "Kaliningrad", "aqi": "no", "key": validApiKey]
            default:
                throw new IllegalArgumentException("Unknown caseName")
        }
    }

    @Unroll
    def "Verify error response for #caseName"() {
        given: "Negative scenario '#caseName'"
        Map<String, Object> queryParams = buildParams(caseName)

        when: "Сall API with these parameters and check"
        steps.verifyErrorWeather(queryParams, caseName)

        then:
        notThrown(Throwable)

        where:
        caseName << ["Param missing", "No match loc", "Invalid key", "Quota exceeded"]
    }

    @Unroll
    def "Verify Asdf JSON expected vs actual Asdf"() {

        given: "Asdf scenario for comparison"
        def city = "Asdf"

        when: "Compare API response vs expected JSON"
        def result = steps.verifyCurrentDifferentWeather(city, "asdf_expected") // тоже не работает

        then: "Actual matches expected"
        result
    }
}