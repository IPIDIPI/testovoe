package dhapr.product.deposit.model

import dhapr.product.deposit.strategy.balace.DepositBalanceStrategy
import dhapr.product.policy.currency.RubOnlyCurrencyPolicy
import java.math.BigDecimal
import java.util.*

internal class RubDeposit(
    name: String,
    initialBalance: BigDecimal,
    balanceStrategy: DepositBalanceStrategy
) : AbstractDeposit(
    name,
    currency = Currency.getInstance("RUB"),
    initialBalance = initialBalance,
    balanceStrategy = balanceStrategy,
    currencyPolicy = RubOnlyCurrencyPolicy
)