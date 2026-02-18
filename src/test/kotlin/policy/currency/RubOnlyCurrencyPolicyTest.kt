package policy.currency

import dhapr.exception.InvalidCurrencyException
import dhapr.product.policy.currency.RubOnlyCurrencyPolicy
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class RubOnlyCurrencyPolicyTest {
    private val policy = RubOnlyCurrencyPolicy

    private val rub = Currency.getInstance("RUB")
    private val usd = Currency.getInstance("USD")
    private val eur = Currency.getInstance("EUR")
    private val gel = Currency.getInstance("GEL")

    @Test
    fun `validate allows RUB`() {
        assertDoesNotThrow { policy.validate(rub) }
    }

    @Test
    fun `validate rejects any non-RUB currency`() {
        listOf(usd, eur, gel).forEach { currency ->
            assertThrows<InvalidCurrencyException> { policy.validate(currency) }
        }
    }
}