package policy.currency

import dhapr.product.policy.currency.MultiCurrencyPolicy
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import java.util.*

class MultiCurrencyPolicyTest {
    private val policy = MultiCurrencyPolicy

    private val rub = Currency.getInstance("RUB")
    private val usd = Currency.getInstance("USD")
    private val eur = Currency.getInstance("EUR")
    private val jpy = Currency.getInstance("JPY")
    private val gbp = Currency.getInstance("GBP")
    private val chf = Currency.getInstance("CHF")
    private val aud = Currency.getInstance("AUD")
    private val cad = Currency.getInstance("CAD")

    @Test
    fun `validate allows all common currencies`() {
        listOf(rub, usd, eur, jpy, gbp, chf, aud, cad).forEach { currency ->
            assertDoesNotThrow { policy.validate(currency) }
        }
    }
}