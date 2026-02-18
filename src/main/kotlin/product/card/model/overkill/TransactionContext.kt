package dhapr.product.card.model.overkill

import java.math.BigDecimal

data class TransactionContext(
    val amount: BigDecimal,
    val initialBalance: BigDecimal,
    var currentBalance: BigDecimal
)