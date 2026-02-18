package policy.currency

import dhapr.exception.InvalidCurrencyException
import dhapr.product.policy.currency.AllowedCurrenciesPolicy
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class AllowedCurrenciesPolicyTest {

    private val usd = Currency.getInstance("USD")
    private val eur = Currency.getInstance("EUR")
    private val rub = Currency.getInstance("RUB")
    private val jpy = Currency.getInstance("JPY")
    private val gbp = Currency.getInstance("GBP")
    private val aud = Currency.getInstance("AUD")

    @Test
    fun `validate allows currency in allowed set`() {
        val policy = AllowedCurrenciesPolicy(setOf(usd, eur))
        assertDoesNotThrow { policy.validate(usd) }
        assertDoesNotThrow { policy.validate(eur) }
    }

    @Test
    fun `validate rejects currency not in allowed set`() {
        val policy = AllowedCurrenciesPolicy(setOf(usd, eur))
        assertThrows<InvalidCurrencyException> { policy.validate(rub) }
    }

    @Test
    fun `validate works with single currency`() {
        val policy = AllowedCurrenciesPolicy(setOf(rub))
        assertDoesNotThrow { policy.validate(rub) }
        assertThrows<InvalidCurrencyException> { policy.validate(usd) }
    }

    @Test
    fun `validate rejects when allowed set is empty`() {
        assertThrows<IllegalArgumentException> { AllowedCurrenciesPolicy(emptySet()) }
    }

    @Test
    fun `validate works for many currencies`() {
        val policy = AllowedCurrenciesPolicy(setOf(usd, eur, rub, jpy, gbp))
        listOf(usd, eur, rub, jpy, gbp).forEach {
            assertDoesNotThrow { policy.validate(it) }
        }
        assertThrows<InvalidCurrencyException> { policy.validate(aud) }
    }
}