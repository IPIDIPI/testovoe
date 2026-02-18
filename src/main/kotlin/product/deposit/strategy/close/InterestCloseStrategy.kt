package dhapr.product.deposit.strategy.close

import dhapr.util.DecimalMath
import dhapr.util.scaleStorage
import java.math.BigDecimal

class InterestCloseStrategy(private val interestRate: BigDecimal) : DepositCloseStrategy {
    override fun applyOnClose(balance: BigDecimal): BigDecimal {
        val interest = DecimalMath.multiply(balance, interestRate).scaleStorage()
        return DecimalMath.add(balance, interest).scaleStorage()
    }
}