import spock.lang.Specification
import spock.lang.Title
import spock.lang.Unroll
import steps.WeatherApiSteps
import stub.DynamicStubs
import stub.WireMockServerManager

@Title("Positive Weather Tests")
class PositiveCurrentWeatherSpec extends Specification {

    WeatherApiSteps steps = new WeatherApiSteps()

    def setupSpec() {
        WireMockServerManager.INSTANCE.start()
        ["London", "Moscow", "Aktau", "Birobidzhan"].each { city ->
            DynamicStubs.INSTANCE.stubPositive(city, "src/test/resources/${city.toLowerCase()}.json")
        }
    }

    def cleanupSpec() {
        WireMockServerManager.INSTANCE.stop()
    }

    @Unroll
    def "Verify current weather for city #city"() {

        given: "Expected weather is loaded from file"
        def expected = steps.getExpectedWeather(city)

        when: "We request current weather via API"
        def actual = steps.getActualWeather(city)

        then: "Response equals expected"
        steps.compareWeather(actual, expected)


        where:
        city << ["London", "Moscow", "Aktau", "Birobidzhan"]
    }
}