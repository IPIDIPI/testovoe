package card

import dhapr.exception.MissingCardBalanceException
import dhapr.product.card.model.AbstractCard
import dhapr.product.policy.currency.MultiCurrencyPolicy
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue


/*
* общие тесты
* */
abstract class AbstractCardTest {

    protected abstract fun createTestCard(): AbstractCard
    protected abstract val primaryCurrency: Currency


    @Test
    fun `initial balance is zero`() {
        val card = createTestCard()
        val (balance, currency) = card.getBalance()
        assertEquals(BigDecimal.ZERO.setScale(2), balance)
        assertEquals(primaryCurrency, currency)
    }

    @Test
    fun `deposit with zero or negative amount throws`() {
        val card = createTestCard()
        val ex1 = assertFailsWith<IllegalArgumentException> { card.deposit(BigDecimal.ZERO, primaryCurrency) }
        assertTrue(ex1.message!!.contains("Amount must be > 0"))

        val ex2 = assertFailsWith<IllegalArgumentException> { card.deposit(BigDecimal("-1.00"), primaryCurrency) }
        assertTrue(ex2.message!!.contains("Amount must be > 0"))
    }

    @Test
    fun `withdraw with zero or negative amount throws`() {
        val card = createTestCard()
        val ex1 = assertFailsWith<IllegalArgumentException> { card.withdraw(BigDecimal.ZERO, primaryCurrency) }
        assertTrue(ex1.message!!.contains("Amount must be > 0"))

        val ex2 = assertFailsWith<IllegalArgumentException> { card.withdraw(BigDecimal("-1.00"), primaryCurrency) }
        assertTrue(ex2.message!!.contains("Amount must be > 0"))
    }

    @Test
    fun `getInternalBalance for missing currency throws`() {
        val card = createTestCard()
        val missingCurrency = if (primaryCurrency.currencyCode != "USD") Currency.getInstance("USD")
        else Currency.getInstance("EUR")
        val e = assertFailsWith<MissingCardBalanceException> {
            card.getBalance(missingCurrency)
        }
        assertTrue(e.message!!.contains("Card balance for ${missingCurrency.currencyCode} is missing"))
    }

    @Test
    fun `name cannot be blank`() {
        val e = assertFailsWith<IllegalArgumentException> {
            object : AbstractCard("", primaryCurrency, MultiCurrencyPolicy) {
                override fun doDeposit(amount: BigDecimal, forCurrency: Currency) = amount to forCurrency
                override fun doWithdraw(amount: BigDecimal, forCurrency: Currency) = amount to forCurrency
            }
        }
        assertTrue(e.message!!.contains("Name cannot be blank"))
    }
}