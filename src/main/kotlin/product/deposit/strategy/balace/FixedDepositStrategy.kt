package dhapr.product.deposit.strategy.balace

import java.math.BigDecimal

object FixedDepositStrategy : DepositBalanceStrategy {
    override fun canDeposit(balance: BigDecimal): Boolean = false
}