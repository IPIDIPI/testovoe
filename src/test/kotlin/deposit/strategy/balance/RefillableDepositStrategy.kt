package deposit.strategy.balance

// тут объединил просто стратегии
import dhapr.product.deposit.strategy.balace.RefillableDepositStrategy
import dhapr.util.scaleStorage
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class RefillableDepositStrategy {
    private val strategy = RefillableDepositStrategy

    @Test
    fun `RefillableDepositStrategy allows deposit`() {
        assertEquals(true, strategy.canDeposit(BigDecimal("0")))
        assertEquals(true, strategy.canDeposit(BigDecimal("1000.1234")))
    }

    /**
     * тут я попроверял честность математики, по факту не нужно
     */
    @Test
    fun `RefillableDepositStrategy applyDeposit increases and normalizes balance`() {
        assertEquals(
            BigDecimal("150.80").scaleStorage(),
            strategy.applyDeposit(BigDecimal("50.6789"), BigDecimal("100.12345"))
        )
        assertEquals(
            BigDecimal("100.12").scaleStorage(),
            strategy.applyDeposit(BigDecimal("0"), BigDecimal("100.12"))
        )
        assertEquals(
            BigDecimal("50.00").scaleStorage(),
            strategy.applyDeposit(BigDecimal("50"), BigDecimal("0"))
        )
        assertEquals(
            BigDecimal("39.50").scaleStorage(),
            strategy.applyDeposit(BigDecimal("50"), BigDecimal("-10.50"))
        )
        assertEquals(
            BigDecimal("50").scaleStorage(),
            strategy.applyDeposit(BigDecimal("50"), BigDecimal("0.0000000000000005"))
        )
        assertEquals(
            BigDecimal("10000000000000050.00").scaleStorage(),
            strategy.applyDeposit(BigDecimal("50"), BigDecimal("9999999999999999.00"))
        )
        assertEquals(
            BigDecimal("1000000000000049.00").scaleStorage(),
            strategy.applyDeposit(BigDecimal("50"), BigDecimal("999999999999999.00"))
        )
        assertEquals(
            BigDecimal("100000000000000000.00").scaleStorage(),
            strategy.applyDeposit(BigDecimal("50"), BigDecimal("99999999999999999.00"))
        )
        assertEquals(
            BigDecimal("50.05").scaleStorage(),
            strategy.applyDeposit(BigDecimal("50"), BigDecimal("0.05"))
        )
        assertEquals(
            BigDecimal("50.01").scaleStorage(),
            strategy.applyDeposit(BigDecimal("50"), BigDecimal("0.006"))
        )
        assertEquals(
            BigDecimal("50.00").scaleStorage(),
            strategy.applyDeposit(BigDecimal("50"), BigDecimal("0.005"))
        )
    }
}

