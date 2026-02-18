package dhapr.product.policy.currency

import java.util.*


/**
 * Политика разрешающая все валюты.
 */
object MultiCurrencyPolicy : CurrencyPolicy {
    override fun validate(currency: Currency) = Unit
}