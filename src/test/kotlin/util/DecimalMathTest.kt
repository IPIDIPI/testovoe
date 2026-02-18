package util

import dhapr.util.DecimalMath
import dhapr.util.scaleStorage
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals


/*
* округления для себя
* */
class DecimalMathTest {
    @Test
    fun `multiply should apply HALF_EVEN rounding with SCALE_STORAGE`() {
        val result1 = DecimalMath.multiply(BigDecimal("10.00"), BigDecimal("1.0005")).scaleStorage()
        assertEquals(BigDecimal("10.00"), result1)
        val result2 = DecimalMath.multiply(BigDecimal("10.01"), BigDecimal("1.0005")).scaleStorage()
        assertEquals(BigDecimal("10.02"), result2)
        val result3 = DecimalMath.multiply(BigDecimal("10.02"), BigDecimal("1.0005")).scaleStorage()
        assertEquals(BigDecimal("10.03"), result3)
    }

    @Test
    fun `add should apply HALF_EVEN rounding with SCALE_STORAGE`() {
        val result1 = DecimalMath.add(BigDecimal("100.00"), BigDecimal("5.005")).scaleStorage()
        assertEquals(BigDecimal("105.00"), result1)
        val result2 = DecimalMath.add(BigDecimal("100.01"), BigDecimal("5.005")).scaleStorage()
        assertEquals(BigDecimal("105.02"), result2)
        val result3 = DecimalMath.add(BigDecimal("100.00"), BigDecimal("5.006")).scaleStorage()
        assertEquals(BigDecimal("105.01"), result3)
        val result4 = DecimalMath.add(BigDecimal("0"), BigDecimal("0.005")).scaleStorage()
        assertEquals(BigDecimal("0.00"), result4)
    }
}