package dhapr.util

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode


const val SCALE_STORAGE = 2

// интернет говорит надо с такой точностью считать, ломается на оч больших числах но для задания не важно считаем что бизнес провел аудит
object CalculationPolicy {
    val MC = MathContext(16, RoundingMode.HALF_EVEN)
}

//тестил математику
object DecimalMath {
    fun add(a: BigDecimal, b: BigDecimal): BigDecimal = a.add(b, CalculationPolicy.MC)
    fun subtract(a: BigDecimal, b: BigDecimal): BigDecimal = a.subtract(b, CalculationPolicy.MC)
    fun multiply(a: BigDecimal, b: BigDecimal): BigDecimal = a.multiply(b, CalculationPolicy.MC)
}

fun BigDecimal.scaleStorage(): BigDecimal =
    this.setScale(SCALE_STORAGE, RoundingMode.HALF_EVEN)

