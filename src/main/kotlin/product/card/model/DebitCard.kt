package dhapr.product.card.model

import dhapr.exception.InsufficientFundsException
import dhapr.product.card.strategy.DebitBalanceStrategy
import dhapr.product.policy.currency.CurrencyPolicy
import dhapr.util.DecimalMath
import java.math.BigDecimal
import java.util.*

/**
 * дебетовая карта в рублях.
 * баланс управляется через [DebitBalanceStrategy]
 * валютнрая политика кастомная
 * списание возможно только в пределах текущего баланса
 *
 */
open class DebitCard(
    name: String,
    currency: Currency,
    currencyPolicy: CurrencyPolicy
) : AbstractCard(
    name = name,
    currency = currency,
    currencyPolicy = currencyPolicy
) {

    override fun doWithdraw(amount: BigDecimal, forCurrency: Currency): Pair<BigDecimal, Currency> {
        val balance = getInternalBalance(forCurrency)
        if (amount > balance) throw InsufficientFundsException(
            "Not enough funds. balance=$balance amount=$amount"
        )
        return DecimalMath.subtract(balance, amount) to forCurrency
    }

    override fun doDeposit(amount: BigDecimal, forCurrency: Currency): Pair<BigDecimal, Currency> {
        val balance = getInternalBalance(forCurrency)
        return DecimalMath.add(balance, amount) to forCurrency
    }
}