package deposit.strategy.close

import dhapr.product.deposit.strategy.close.InterestCloseStrategy
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class InterestCloseStrategyTest {
    /**
     * Стратегия начисления процентов корректно считает и нормализует баланс
     */
    @Test
    fun `InterestCloseStrategy applies correct interest`() {
        val strategy = InterestCloseStrategy(BigDecimal("0.1"))
        val result = strategy.applyOnClose(BigDecimal("200.55"))
        assertEquals(BigDecimal("220.61"), result)
    }

    /**
     * Стратегия начисления процентов с дробным процентом
     */
    @Test
    fun `InterestCloseStrategy with fractional interest`() {
        val strategy = InterestCloseStrategy(BigDecimal("0.1234"))
        val result = strategy.applyOnClose(BigDecimal("123.45"))
        assertEquals(BigDecimal("138.68"), result)
    }

    /**
     * Нулевой процент оставляет баланс, но нормализует
     */
    @Test
    fun `InterestCloseStrategy with zero interest`() {
        val strategy = InterestCloseStrategy(BigDecimal.ZERO)
        val result = strategy.applyOnClose(BigDecimal("100.55"))
        assertEquals(BigDecimal("100.55"), result)
    }
}