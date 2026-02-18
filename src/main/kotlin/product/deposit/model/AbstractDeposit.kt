package dhapr.product.deposit.model

import dhapr.exception.DepositClosedException
import dhapr.product.AbstractProduct
import dhapr.product.BalanceAware
import dhapr.product.deposit.strategy.balace.DepositBalanceStrategy
import dhapr.product.policy.currency.CurrencyPolicy
import dhapr.util.scaleStorage
import java.math.BigDecimal
import java.util.*

/**
 * Абстрактный базовый класс вклада
 * Тут я бы еще поигрался с возможностью создания вклада без денег, но бизнес решил что пустой вклад создавать нельзя
 */
abstract class AbstractDeposit(
    name: String,
    private val currency: Currency,
    initialBalance: BigDecimal,
    private val balanceStrategy: DepositBalanceStrategy,
    private val currencyPolicy: CurrencyPolicy
) : AbstractProduct(name),
    BalanceAware {
    private var balance: BigDecimal = initialBalance.scaleStorage()
    private var closed: Boolean = false

    init {
        require(initialBalance > BigDecimal.ZERO) { "Initial balance must be > 0" }
        currencyPolicy.validate(currency)
    }

    /**
     * Пополнение депозита
     * Проверка валюты и стратегии
     */
    fun deposit(amount: BigDecimal, currency: Currency) {
        require(amount > BigDecimal.ZERO) { "Deposit must be > 0" }
        check(!closed) { throw DepositClosedException("Deposit is closed") }
        currencyPolicy.validate(currency)
        balance = balanceStrategy.applyDeposit(amount, balance)
    }

    /**
     * Баланс депозита
     */
    override fun getBalance(): Pair<BigDecimal, Currency> {
        check(!closed) { throw DepositClosedException("Deposit is closed") }
        return balance to currency
    }

    /**
     * Закрытие депозита
     */
    open fun close(): BigDecimal {
        check(!closed) { throw DepositClosedException("Deposit is closed") }
        closed = true
        return balance
    }
}