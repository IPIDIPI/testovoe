package deposit.strategy.balance

import dhapr.product.deposit.strategy.balace.FixedDepositStrategy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.test.assertEquals

class FixedDepositStrategy {
    private val strategy = FixedDepositStrategy

    @Test
    fun `FixedDepositStrategy forbids deposit`() {
        assertEquals(false, strategy.canDeposit(BigDecimal("100")))
        assertThrows<IllegalStateException> {
            strategy.applyDeposit(BigDecimal("50"), BigDecimal("100"))
        }
    }
}