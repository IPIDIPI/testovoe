package dhapr.product.card.model.overkill

interface TransactionalStrategy {
    fun applyDeposit(context: TransactionContext)
    fun applyWithdrawal(context: TransactionContext)
}