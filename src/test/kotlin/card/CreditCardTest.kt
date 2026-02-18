package card

import dhapr.exception.InsufficientFundsException
import dhapr.product.card.model.CreditCard
import dhapr.product.card.model.overkill.CompositeCreditStrategy
import dhapr.product.card.model.overkill.CreditLimitStrategy
import dhapr.product.card.model.overkill.WithdrawalFeeStrategy
import dhapr.product.card.strategy.CreditBalanceStrategy
import dhapr.product.policy.currency.MultiCurrencyPolicy
import dhapr.product.policy.currency.RubOnlyCurrencyPolicy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.util.*
import kotlin.test.assertEquals

class CreditCardTest() : AbstractCardTest() {


    private val usd = Currency.getInstance("USD")
    override val primaryCurrency: Currency = usd
    override fun createTestCard(): CreditCard = CreditCard(
        name = "MyCredit",
        currency = usd,
        currencyPolicy = MultiCurrencyPolicy,
        interestRate = BigDecimal("0.05"),
        balanceStrategy = CreditBalanceStrategy
    )

    @Test
    fun `deposit increases balance`() {
        val card = createTestCard()
        card.deposit(BigDecimal("200.00"), usd)
        val (balance, _) = card.getBalance()
        assertEquals(BigDecimal("200.00"), balance)
    }

    @Test
    fun `withdraw decreases balance`() {
        val card = createTestCard()
        card.deposit(BigDecimal("200.00"), usd)
        card.withdraw(BigDecimal("50.00"), usd)
        val (balance, _) = card.getBalance()
        assertEquals(BigDecimal("150.00"), balance)
    }

    @Test
    fun `getDebt returns zero for positive balance`() {
        val card = createTestCard()
        card.deposit(BigDecimal("100.00"), usd)
        val (debt, _) = card.getDebt(usd)
        assertEquals(BigDecimal("0.00"), debt)
    }

    @Test
    fun `getDebt returns positive for negative balance`() {
        val card = createTestCard()
        card.withdraw(BigDecimal("50.00"), usd) // balance -50
        val (debt, _) = card.getDebt(usd)
        assertEquals(BigDecimal("50.00"), debt)
    }

    //крутая кредитка
    @Test
    fun testSuperCard() {
        val rub = Currency.getInstance("RUB")
        val strategy = CompositeCreditStrategy(
            listOf(
                WithdrawalFeeStrategy(0.01.toBigDecimal()),
                CreditLimitStrategy(BigDecimal(100))
            )
        )
        val card = CreditCard(
            "overkill",
            rub,
            RubOnlyCurrencyPolicy,
            BigDecimal("0.1234"),
            strategy
        )

        card.deposit(BigDecimal("100.00"), rub)
        assertEquals(BigDecimal("100.00") to rub, card.getBalance(rub))
        assertEquals(Pair(BigDecimal("0.00"), rub), card.getDebt(rub))
        //  снятие до 0 без комиссии
        card.withdraw(BigDecimal("100"), rub)
        assertEquals(BigDecimal("0.00") to rub, card.getBalance(rub))
        assertEquals(Pair(BigDecimal("0.00"), rub), card.getDebt(rub))
        // уходим в долг берем коммисию
        card.withdraw(BigDecimal("90"), rub)
        assertEquals(BigDecimal("-90.90") to rub, card.getBalance(rub))
        assertEquals(Pair(BigDecimal("90.90"), rub), card.getDebt(rub))
        // снимаем что бы попасть в лимит но коммисия не позволяет
        assertThrows<InsufficientFundsException> { card.withdraw(BigDecimal("9.10"), rub) }
    }

}