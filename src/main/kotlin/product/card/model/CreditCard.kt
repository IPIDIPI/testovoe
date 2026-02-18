package dhapr.product.card.model

import dhapr.product.card.strategy.BalanceChangeStrategy
import dhapr.product.card.strategy.CreditBalanceStrategy
import dhapr.product.policy.currency.CurrencyPolicy
import dhapr.product.policy.currency.RubOnlyCurrencyPolicy
import java.math.BigDecimal
import java.util.*

/**
 * кредитная карта с заданной ставкой и управлением баланса
 *
 * баланс ведется через [CreditBalanceStrategy],
 * все операции в RUB [RubOnlyCurrencyPolicy].
 *
 * @param name имя держателя карты
 * @param interestRate процентная ставка ну тут просто заглушка, писать списание процентов не стал в оверкил вынес в стратегию
 */
class CreditCard(
    name: String,
    currency: Currency,
    currencyPolicy: CurrencyPolicy,
    interestRate: BigDecimal,
    private val balanceStrategy: BalanceChangeStrategy,
) : AbstractCard(
    name = name,
    currency = currency,
    currencyPolicy = currencyPolicy
) {

    init {
        require(interestRate >= BigDecimal.ZERO) { "Interest rate cannot be negative" }
    }

    /**
     * текущий долг по карте
     */
    fun getDebt(forCurrency: Currency): Pair<BigDecimal, Currency> {
        val balance = getInternalBalance(forCurrency)
        return (if (balance < BigDecimal.ZERO) balance.abs() else BigDecimal("0.00")) to forCurrency
    }


    override fun doDeposit(amount: BigDecimal, forCurrency: Currency): Pair<BigDecimal, Currency> {
        val currentBalance = getInternalBalance(forCurrency)
        return balanceStrategy.applyDeposit(amount, currentBalance) to forCurrency
    }

    override fun doWithdraw(amount: BigDecimal, forCurrency: Currency): Pair<BigDecimal, Currency> {
        val currentBalance = getInternalBalance(forCurrency)
        return balanceStrategy.applyWithdrawal(amount, currentBalance) to forCurrency
    }
}