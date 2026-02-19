package api.client

import io.qameta.allure.restassured.AllureRestAssured
import io.restassured.RestAssured

abstract class InitApiClient {
    init {
        RestAssured.replaceFiltersWith(AllureRestAssured())
    }
}