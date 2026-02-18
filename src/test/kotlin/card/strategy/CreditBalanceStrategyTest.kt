package card.strategy

import dhapr.product.card.strategy.CreditBalanceStrategy
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


/**
 * Credit balance strategy test
 *
 */
class CreditBalanceStrategyTest {

    private val strategy = CreditBalanceStrategy

    @Test
    fun `deposit adds amount`() {
        val result = strategy.applyDeposit(BigDecimal("50.00"), BigDecimal("100.00"))
        assertEquals(BigDecimal("150.00"), result)
    }

    @Test
    fun `withdraw subtracts amount`() {
        val result = strategy.applyWithdrawal(BigDecimal("30.00"), BigDecimal("100.00"))
        assertEquals(BigDecimal("70.00"), result)
    }

    @Test
    fun `canWithdraw always returns true`() {
        assertTrue(strategy.canWithdraw(BigDecimal("1000.00"), BigDecimal("0.00")))
        assertTrue(strategy.canWithdraw(BigDecimal("0.01"), BigDecimal("-100.00")))
    }

}
