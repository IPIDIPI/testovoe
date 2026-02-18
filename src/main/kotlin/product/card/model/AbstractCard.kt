package dhapr.product.card.model

import dhapr.exception.MissingCardBalanceException
import dhapr.product.AbstractProduct
import dhapr.product.BalanceAware
import dhapr.product.policy.currency.CurrencyPolicy
import dhapr.util.scaleStorage
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Базовая реализация банковской карты.
 *
 * @param balances
 * баланс каты такой что бы иметь возможность расширяться я бы вообще выделил в отдельную сущность но хотелось не слишком вылезать из ТЗ
 * мультисчетные карты (наоборот уже было бы слишком заморочено)
 * не забыли про потокобезопасность баланса (но на этом многопоточка заканчивается, там можно для всех валют делать отдельно и прочее)
 * про создание баланса валюты при пополнении\снятии я бы еще подумал вынести, но пока там будет
 *
 */
abstract class AbstractCard(
    name: String,
    private val currency: Currency,
    private val currencyPolicy: CurrencyPolicy
) : AbstractProduct(name),
    BalanceAware {
    private val balances: MutableMap<Currency, BigDecimal> = ConcurrentHashMap()

    init {
        balances[currency] = BigDecimal.ZERO.scaleStorage()
        currencyPolicy.validate(currency)
    }

    /*
    * немного гад функция проверяет создает и нормализует но тут просто
    * */
    private fun applyOperation(
        amount: BigDecimal,
        targetCurrency: Currency,
        operation: (BigDecimal, Currency) -> Pair<BigDecimal, Currency>
    ) {
        require(amount > BigDecimal.ZERO) { "Amount must be > 0" }
        currencyPolicy.validate(targetCurrency)
        balances.putIfAbsent(targetCurrency, BigDecimal.ZERO.scaleStorage())
        val (newBalance, resultCurrency) = operation(amount, targetCurrency)
        balances[resultCurrency] = newBalance.scaleStorage()
    }

    /**
     * Пополнение баланса в указанной валюте
     */
    fun deposit(depositAmount: BigDecimal, depositCurrency: Currency) =
        applyOperation(depositAmount, depositCurrency) { amount, currency ->
            doDeposit(amount, currency)
        }

    /**
     * Списание в указанной валюте
     */
    fun withdraw(withdrawAmount: BigDecimal, withdrawCurrency: Currency) =
        applyOperation(withdrawAmount, withdrawCurrency) { amount, currency ->
            doWithdraw(amount, currency)
        }

    // внутренний метод
    protected fun getInternalBalance(currency: Currency): BigDecimal {
        val balance = balances[currency]
            ?: throw MissingCardBalanceException("Card balance for ${currency.currencyCode} is missing")
        return balance
    }

    override fun getBalance(): Pair<BigDecimal, Currency> = getInternalBalance(currency) to currency

    fun getBalance(forCurrency: Currency): Pair<BigDecimal, Currency> =
        getInternalBalance(forCurrency) to forCurrency

    /**
     * Должен вернуть НОВЫЙ(итоговый) баланс и валюту баланса после списания.
     */
    protected abstract fun doWithdraw(amount: BigDecimal, forCurrency: Currency): Pair<BigDecimal, Currency>

    /**
     * Должен вернуть НОВЫЙ(итоговый) баланс и валюту баланса после пополнения.
     */
    protected abstract fun doDeposit(amount: BigDecimal, forCurrency: Currency): Pair<BigDecimal, Currency>

}
