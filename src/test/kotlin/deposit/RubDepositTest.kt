package deposit

import dhapr.exception.DepositClosedException
import dhapr.exception.InvalidCurrencyException
import dhapr.product.deposit.model.AbstractDeposit
import dhapr.product.deposit.model.RubDeposit
import dhapr.product.deposit.strategy.balace.DepositBalanceStrategy
import dhapr.product.deposit.strategy.balace.RefillableDepositStrategy
import dhapr.util.scaleStorage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.util.*
import kotlin.test.assertEquals


class RubDepositTest : AbstractDepositTest() {

    override val currency: Currency = Currency.getInstance("RUB")

    override fun createDeposit(initial: BigDecimal): AbstractDeposit =
        RubDeposit(
            name = "TestDeposit",
            initialBalance = initial,
            balanceStrategy = RefillableDepositStrategy
        )

    private lateinit var balanceStrategy: DepositBalanceStrategy
    private lateinit var deposit: RubDeposit

    @BeforeEach
    fun setUp() {
        balanceStrategy = RefillableDepositStrategy
        deposit = RubDeposit("RUB Deposit", BigDecimal("200.555"), balanceStrategy)
    }

    /**
     * Инициализация с валютой RUB
     */
    @Test
    fun `initialize with rub currency`() {
        val (balance, currency) = deposit.getBalance()
        assertEquals(BigDecimal("200.56").scaleStorage(), balance)
        assertEquals(Currency.getInstance("RUB"), currency)
    }

    /**
     * Пополнение депозита увеличивает баланс и нормализует его
     */
    @Test
    fun `deposit funds increases and normalizes balance`() {
        deposit.deposit(BigDecimal("50.123"), Currency.getInstance("RUB"))
        val (balance, _) = deposit.getBalance()
        assertEquals(BigDecimal("250.68").scaleStorage(), balance)
    }

    /**
     * Попытка пополнения в другой валюте бросает исключение
     */
    @Test
    fun `deposit non rub currency throws exception`() {
        assertThrows<InvalidCurrencyException> {
            deposit.deposit(BigDecimal("50"), Currency.getInstance("USD"))
        }
    }

    /**
     * Закрытие депозита работает и блокирует дальнейшие операции
     */
    @Test
    fun `close deposit blocks further operations`() {
        val closedBalance = deposit.close()
        assertEquals(BigDecimal("200.56").scaleStorage(), closedBalance)

        assertThrows<DepositClosedException> { deposit.deposit(BigDecimal("10"), Currency.getInstance("RUB")) }
        assertThrows<DepositClosedException> { deposit.getBalance() }
        assertThrows<DepositClosedException> { deposit.close() }
    }

    /**
     * Нельзя создать депозит с нулевым или отрицательным балансом
     */
    @Test
    fun `cannot create deposit with zero or negative_balance`() {
        assertThrows<IllegalArgumentException> {
            RubDeposit("zero", BigDecimal.ZERO, RefillableDepositStrategy)
        }
        assertThrows<IllegalArgumentException> {
            RubDeposit("negative", BigDecimal("-100"), RefillableDepositStrategy)
        }
    }
}