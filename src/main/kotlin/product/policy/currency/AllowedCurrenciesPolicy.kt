package dhapr.product.policy.currency

import dhapr.exception.InvalidCurrencyException
import java.util.*

/**
 * Политика допустимых валют для карты
 */
class AllowedCurrenciesPolicy(private val allowed: Set<Currency>) : CurrencyPolicy {
    init {
        require(allowed.isNotEmpty()) { "Allowed currencies set cannot be empty" }
    }

    override fun validate(currency: Currency) {
        if (currency !in allowed) {
            throw InvalidCurrencyException(
                "Currency ${currency.currencyCode} not allowed for this card"
            )
        }
    }
}