package dhapr.product.deposit.strategy.close

import java.math.BigDecimal

/**
 * Пример стратегии: просто возвращает текущий баланс
 */
object SimpleCloseStrategy : DepositCloseStrategy {
    override fun applyOnClose(balance: BigDecimal): BigDecimal = balance
}