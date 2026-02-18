package card

import dhapr.exception.InsufficientFundsException
import dhapr.product.card.model.DebitCard
import dhapr.product.policy.currency.RubOnlyCurrencyPolicy
import java.math.BigDecimal
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class DebitCardTest : AbstractCardTest() {
    private val cardName = "MyDebitCard"
    override val primaryCurrency: Currency = Currency.getInstance("RUB")
    override fun createTestCard(): DebitCard = DebitCard(cardName, primaryCurrency, RubOnlyCurrencyPolicy)


    @Test
    fun `deposit increases balance`() {
        val card = createTestCard()
        card.deposit(BigDecimal("100.00"), primaryCurrency)
        val (balance, _) = card.getBalance()
        assertEquals(BigDecimal("100.00"), balance)
    }

    @Test
    fun `withdraw decreases balance`() {
        val card = createTestCard()
        card.deposit(BigDecimal("200.00"), primaryCurrency)
        card.withdraw(BigDecimal("50.00"), primaryCurrency)
        val (balance, _) = card.getBalance()
        assertEquals(BigDecimal("150.00"), balance)
    }

    @Test
    fun `withdraw more than balance throws`() {
        val card = createTestCard()
        card.deposit(BigDecimal("100.00"), primaryCurrency)
        val e = assertFailsWith<InsufficientFundsException> {
            card.withdraw(BigDecimal("150.00"), primaryCurrency)
        }
        assertTrue(e.message!!.contains("Not enough funds"))
    }
}