package dhapr.product.card.strategy

import dhapr.exception.InsufficientFundsException
import dhapr.util.DecimalMath
import dhapr.util.scaleStorage
import java.math.BigDecimal

/**
 * Запрещает списание средств больше текущего баланса
 */
object DebitBalanceStrategy : BalanceChangeStrategy {

    // нельзя в минус
    override fun canWithdraw(
        amount: BigDecimal,
        balance: BigDecimal
    ): Boolean {
        return balance >= amount
    }

    override fun applyDeposit(
        amount: BigDecimal,
        balance: BigDecimal
    ): BigDecimal {
        return DecimalMath.add(balance, amount).scaleStorage()
    }

    override fun applyWithdrawal(
        amount: BigDecimal,
        balance: BigDecimal
    ): BigDecimal {
        if (!canWithdraw(amount, balance)) {
            throw InsufficientFundsException(
                "Not enough funds. balance=$balance amount=$amount"
            )
        }
        return DecimalMath.subtract(balance, amount).scaleStorage()
    }
}