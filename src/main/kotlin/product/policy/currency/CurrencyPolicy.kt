package dhapr.product.policy.currency

import java.util.*


/**
 * Контракт проверки валют для карты
 */
fun interface CurrencyPolicy {
    fun validate(currency: Currency)
}