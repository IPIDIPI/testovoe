package models.current

data class CurrentWeatherResponse(
    val location: Location,
    val current: CurrentWeather
)