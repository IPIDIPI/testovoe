package card

import dhapr.exception.InsufficientFundsException
import dhapr.exception.InvalidCurrencyException
import dhapr.product.card.model.RubDebitCard
import java.math.BigDecimal
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class RubDebitCardTest : AbstractCardTest() {

    private val rub = Currency.getInstance("RUB")
    private val cardName = "RUBCard"
    override val primaryCurrency: Currency = rub
    override fun createTestCard(): RubDebitCard = RubDebitCard(cardName)


    @Test
    fun `deposit in RUB works`() {
        val card = createTestCard()
        card.deposit(BigDecimal("100.00"), rub)
        val (balance, _) = card.getBalance()
        assertEquals(BigDecimal("100.00"), balance)
    }

    @Test
    fun `withdraw in RUB works`() {
        val card = createTestCard()
        card.deposit(BigDecimal("200.00"), rub)
        card.withdraw(BigDecimal("50.00"), rub)
        val (balance, _) = card.getBalance()
        assertEquals(BigDecimal("150.00"), balance)
    }

    @Test
    fun `withdraw more than balance throws`() {
        val card = createTestCard()
        card.deposit(BigDecimal("100.00"), rub)
        val e = assertFailsWith<InsufficientFundsException> {
            card.withdraw(BigDecimal("150.00"), rub)
        }
        assertTrue(e.message!!.contains("Not enough funds"))
    }

    @Test
    fun `using non-RUB currency throws InvalidCurrencyException`() {
        val card = createTestCard()
        val usd = Currency.getInstance("USD")
        val exDeposit = assertFailsWith<InvalidCurrencyException> {
            card.deposit(BigDecimal("10.00"), usd)
        }
        assertTrue(exDeposit.message!!.contains("Only RUB is allowed"))

        val exWithdraw = assertFailsWith<InvalidCurrencyException> {
            card.withdraw(BigDecimal("10.00"), usd)
        }
        assertTrue(exWithdraw.message!!.contains("Only RUB is allowed"))
    }
}