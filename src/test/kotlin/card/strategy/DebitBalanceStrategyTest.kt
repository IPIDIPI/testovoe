package card.strategy

import dhapr.exception.InsufficientFundsException
import dhapr.product.card.strategy.DebitBalanceStrategy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Debit balance strategy test
 *
 * @constructor Create empty Debit balance strategy test
 */
class DebitBalanceStrategyTest {

    private val strategy = DebitBalanceStrategy

    /**
     * Deposit adds amount to balance with correct storage rounding
     *
     */
    @Test
    fun `deposit adds amount to balance with correct storage rounding`() {
        val initial = BigDecimal("100.123456")
        val deposit = BigDecimal("50.654321")
        val result = strategy.applyDeposit(deposit, initial)
        assertEquals(BigDecimal("150.78"), result)
    }

    /**
     * Withdraw subtracts amount from balance with correct storage rounding
     *
     */
    @Test
    fun `withdraw subtracts amount from balance with correct storage rounding`() {
        val initial = BigDecimal("200.00")
        val withdraw = BigDecimal("75.55555")
        val result = strategy.applyWithdrawal(withdraw, initial)
        assertEquals(BigDecimal("124.44"), result)
    }

    /**
     * Cannot withdraw more than balance throws insufficient funds exception
     *
     */
    @Test
    fun `cannot withdraw more than balance throws InsufficientFundsException`() {
        val initial = BigDecimal("50.00")
        val withdraw = BigDecimal("60.00")
        assertThrows<InsufficientFundsException> { strategy.applyWithdrawal(withdraw, initial) }
    }

    /**
     * Can withdraw returns true when balance is greater than or equal to amount
     *
     */
    @Test
    fun `canWithdraw returns true when balance is greater than or equal to amount`() {
        assertTrue(strategy.canWithdraw(BigDecimal("50.00"), BigDecimal("50.00")))
        assertTrue(strategy.canWithdraw(BigDecimal("30.00"), BigDecimal("50.00")))
    }

    /**
     * Can withdraw returns false when balance is less than amount
     *
     */
    @Test
    fun `canWithdraw returns false when balance is less than amount`() {
        assertFalse(strategy.canWithdraw(BigDecimal("60.00"), BigDecimal("50.00")))
    }
}