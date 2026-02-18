package dhapr.product.card.strategy

import dhapr.exception.InsufficientFundsException
import dhapr.util.DecimalMath
import java.math.BigDecimal

/**
 * Позволяет списание средств с овердрафтом
 */
object CreditBalanceStrategy : BalanceChangeStrategy {


    //можно в минус уйти
    override fun canWithdraw(
        amount: BigDecimal, balance: BigDecimal
    ): Boolean {
        return true
    }

    override fun applyDeposit(
        amount: BigDecimal, balance: BigDecimal
    ): BigDecimal {
        return DecimalMath.add(balance, amount)
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
        return DecimalMath.subtract(balance, amount)
    }
}