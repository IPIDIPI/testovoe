package dhapr.product.card.model

import dhapr.product.policy.currency.RubOnlyCurrencyPolicy
import java.util.*

class RubDebitCard(
    name: String,
) : DebitCard(
    name = name,
    currency = Currency.getInstance("RUB"),
    currencyPolicy = RubOnlyCurrencyPolicy
) {
    /*
    * И вот тут мы можем реализовавать какие то новые штуки
    * */
}