package dhapr.product.card.strategy

import java.math.BigDecimal

/**
 * Стратегия изменения баланса карты
 * Определяет правила пополнения списания и проверки возможности списания
 * Реализации инкапсулируют бизнес-логику продукта
 */
interface BalanceChangeStrategy {
    /**
     * Проверяет, возможно ли списание средств
     */
    fun canWithdraw(amount: BigDecimal, balance: BigDecimal): Boolean

    /**
     * Применяет пополнение баланса.
     */
    fun applyDeposit(amount: BigDecimal, balance: BigDecimal): BigDecimal

    /**
     * Применяет списание средств.
     */
    fun applyWithdrawal(amount: BigDecimal, balance: BigDecimal): BigDecimal

}