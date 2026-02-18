package dhapr.product

import java.math.BigDecimal
import java.util.*

interface BalanceAware {
    fun getBalance(): Pair<BigDecimal, Currency>
}