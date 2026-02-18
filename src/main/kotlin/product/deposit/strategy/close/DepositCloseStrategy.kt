package dhapr.product.deposit.strategy.close

import java.math.BigDecimal

fun interface DepositCloseStrategy {
    /**
     * Рассчитывает сумму при закрытии депозита
     * @param balance текущий баланс депозита
     * @return сумма, которая будет возвращена клиенту
     */
    fun applyOnClose(balance: BigDecimal): BigDecimal
}