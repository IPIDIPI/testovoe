package dhapr.product.deposit.strategy.balace

import java.math.BigDecimal

object RefillableDepositStrategy : DepositBalanceStrategy {
    override fun canDeposit(balance: BigDecimal): Boolean = true
}