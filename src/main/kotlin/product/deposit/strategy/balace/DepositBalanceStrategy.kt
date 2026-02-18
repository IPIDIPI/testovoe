package dhapr.product.deposit.strategy.balace


import dhapr.util.DecimalMath
import dhapr.util.scaleStorage
import java.math.BigDecimal

/**
 * Стратегия изменения баланса для вкладов
 */
interface DepositBalanceStrategy {

    /**
     * Проверяет, можно ли пополнять вклад
     */
    fun canDeposit(balance: BigDecimal): Boolean

    /**
     * Применяет пополнение баланса
     */
    fun applyDeposit(amount: BigDecimal, balance: BigDecimal): BigDecimal {
        if (!canDeposit(balance)) {
            throw IllegalStateException("Deposit cannot be replenished")
        }
        return DecimalMath.add(balance, amount).scaleStorage()
    }
}