package card.strategy

import dhapr.exception.InsufficientFundsException
import dhapr.product.card.strategy.LimitCreditBalanceStrategy
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LimitCreditBalanceStrategyTest {
    private val limit = BigDecimal("100.00")
    private val strategy = LimitCreditBalanceStrategy(limit)

    @Test
    fun `withdraw within limit allowed`() {
        val result = strategy.applyWithdrawal(BigDecimal("50.00"), BigDecimal("0.00"))
        assertEquals(BigDecimal("-50.00"), result)
    }

    @Test
    fun `withdraw exceeding limit throws`() {
        val e = assertFailsWith<InsufficientFundsException> {
            strategy.applyWithdrawal(BigDecimal("150.00"), BigDecimal("0.00"))
        }
        assertTrue(e.message!!.contains("Not enough funds"))
    }

    @Test
    fun `deposit adds to balance`() {
        val result = strategy.applyDeposit(BigDecimal("30.00"), BigDecimal("20.00"))
        assertEquals(BigDecimal("50.00"), result)
    }

    @Test
    fun `canWithdraw respects limit`() {
        assertTrue(strategy.canWithdraw(BigDecimal("50.00"), BigDecimal("0.00")))
        assertFalse(strategy.canWithdraw(BigDecimal("150.00"), BigDecimal("0.00")))
    }
}