package dhapr.product.card.strategy

import dhapr.exception.InsufficientFundsException
import dhapr.util.DecimalMath
import java.math.BigDecimal

class LimitCreditBalanceStrategy(private val limit: BigDecimal) : BalanceChangeStrategy {

    init {
        require(limit > BigDecimal.ZERO) { "limit rate cannot be negative" }
    }

    //снять не больше чем лимит
    override fun canWithdraw(
        amount: BigDecimal, balance: BigDecimal
    ): Boolean {
        return balance - amount >= -limit
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