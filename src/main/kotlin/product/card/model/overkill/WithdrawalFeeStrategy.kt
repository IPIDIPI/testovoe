package dhapr.product.card.model.overkill


import dhapr.util.DecimalMath
import dhapr.util.scaleStorage
import java.math.BigDecimal

class WithdrawalFeeStrategy(private val feePercent: BigDecimal) : TransactionalStrategy {
    override fun applyDeposit(context: TransactionContext) {}

    override fun applyWithdrawal(context: TransactionContext) {
        if (DecimalMath.subtract(context.currentBalance, context.amount) < BigDecimal.ZERO) {
            val fee = DecimalMath.multiply(context.amount, feePercent).scaleStorage().min(BigDecimal("100"))
            context.currentBalance = DecimalMath.subtract(context.currentBalance, fee).scaleStorage()
        }
    }
}