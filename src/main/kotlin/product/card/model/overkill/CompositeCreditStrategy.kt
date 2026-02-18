package dhapr.product.card.model.overkill

import dhapr.exception.InsufficientFundsException
import dhapr.product.card.strategy.BalanceChangeStrategy
import java.math.BigDecimal


/**
 * прикольная штука extensions reified <*> и вот это все можно на собесе давать если не жалко человека
 */
inline fun <reified T : Throwable> Result<*>.successUnless(): Boolean {
    return getOrElse { e ->
        if (e is T) false
        else throw e
    }.let { true }
}

class CompositeCreditStrategy(
    private val strategies: List<TransactionalStrategy>
) : BalanceChangeStrategy {

    override fun canWithdraw(amount: BigDecimal, balance: BigDecimal): Boolean {
        val context = TransactionContext(amount, balance, balance)
        return runCatching { //прикольная штука
            strategies.forEach { it.applyWithdrawal(context) }
        }.successUnless<InsufficientFundsException>()
    }

    override fun applyDeposit(amount: BigDecimal, balance: BigDecimal): BigDecimal {
        val context = TransactionContext(amount, balance, balance)
        strategies.forEach { it.applyDeposit(context) }
        return context.currentBalance
    }

    override fun applyWithdrawal(amount: BigDecimal, balance: BigDecimal): BigDecimal {
        val context = TransactionContext(amount, balance, balance)
        strategies.forEach { it.applyWithdrawal(context) }
        return context.currentBalance
    }
}
