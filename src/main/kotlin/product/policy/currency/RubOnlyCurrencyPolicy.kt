package dhapr.product.policy.currency

import dhapr.exception.InvalidCurrencyException
import java.util.*

/**
 * Политика разрешающая только рубли
 */
object RubOnlyCurrencyPolicy : CurrencyPolicy {
    override fun validate(currency: Currency) {
        if (currency.currencyCode != "RUB") {
            throw InvalidCurrencyException(
                "Only RUB is allowed for this card, but was: ${currency.currencyCode}"
            )
        }
    }
}