package dhapr.product.deposit.model

import dhapr.product.deposit.strategy.balace.DepositBalanceStrategy
import dhapr.product.deposit.strategy.close.DepositCloseStrategy
import dhapr.product.policy.currency.CurrencyPolicy
import java.math.BigDecimal
import java.util.*

/**
 * депозит с начислением процентов при закрытии
 */
class InterestDeposit(
    name: String,
    currency: Currency,
    initialBalance: BigDecimal,
    balanceStrategy: DepositBalanceStrategy,
    currencyPolicy: CurrencyPolicy,
    private val closeStrategy: DepositCloseStrategy
) : AbstractDeposit(
    name,
    currency,
    initialBalance,
    balanceStrategy,
    currencyPolicy = currencyPolicy,
) {
    /**
     * Закрытие депозита с применением стратегии
     */
    override fun close(): BigDecimal {
        val currentBalance = super.getBalance().first
        val finalAmount = closeStrategy.applyOnClose(currentBalance)
        super.close() // помечаем депозит как закрытый
        return finalAmount
    }
}