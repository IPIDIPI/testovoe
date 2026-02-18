package deposit.strategy.close

import dhapr.product.deposit.strategy.close.SimpleCloseStrategy
import dhapr.util.scaleStorage
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

// тут объединил потому что проверять нечего
class SimpleCloseStrategyTest {

    val strategy = SimpleCloseStrategy

    /**
     * Простая стратегия закрытия возвращает текущий баланс
     */
    @Test
    fun `SimpleCloseStrategy returns same balance`() {
        val result = strategy.applyOnClose(BigDecimal("100.12"))
        assertEquals(BigDecimal("100.12"), result.scaleStorage())
    }


}