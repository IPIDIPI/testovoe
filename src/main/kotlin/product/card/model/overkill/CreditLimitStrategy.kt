package dhapr.product.card.model.overkill

import dhapr.exception.InsufficientFundsException
import dhapr.util.DecimalMath
import dhapr.util.SCALE_STORAGE
import dhapr.util.scaleStorage
import java.math.BigDecimal
import java.math.RoundingMode

class CreditLimitStrategy(creditLimit: BigDecimal) : TransactionalStrategy {

    private val limit = creditLimit.setScale(SCALE_STORAGE, RoundingMode.HALF_EVEN)
    private val minBalance = limit.negate()

    override fun applyDeposit(context: TransactionContext) {
        context.currentBalance = DecimalMath.add(context.currentBalance, context.amount).scaleStorage()
    }

    override fun applyWithdrawal(context: TransactionContext) {
        val newBalance = DecimalMath.subtract(context.currentBalance, context.amount).scaleStorage()
        if (newBalance < minBalance) {
            throw InsufficientFundsException(
                "Credit limit exceeded: limit=$limit, balance=${context.initialBalance}, amount=${context.amount}"
            )
        }
        context.currentBalance = newBalance
    }
}
