package deposit

import dhapr.exception.DepositClosedException
import dhapr.product.deposit.model.AbstractDeposit
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

abstract class AbstractDepositTest {

    protected abstract fun createDeposit(initial: BigDecimal): AbstractDeposit
    protected abstract val currency: Currency

    @Test
    fun `initial balance must be positive`() {
        val e = assertFailsWith<IllegalArgumentException> {
            createDeposit(BigDecimal.ZERO)
        }
        assertTrue(e.message!!.contains("Initial balance must be > 0"))
    }

    @Test
    fun `balance equals initial`() {
        val deposit = createDeposit(BigDecimal("100.00"))
        val (balance, curr) = deposit.getBalance()
        assertEquals(BigDecimal("100.00"), balance)
        assertEquals(currency, curr)
    }

    @Test
    fun `deposit increases balance`() {
        val deposit = createDeposit(BigDecimal("100.00"))
        deposit.deposit(BigDecimal("50.00"), currency)
        val (balance, _) = deposit.getBalance()
        assertEquals(BigDecimal("150.00"), balance)
    }

    @Test
    fun `deposit zero or negative throws`() {
        val deposit = createDeposit(BigDecimal("100.00"))

        val ex1 = assertFailsWith<IllegalArgumentException> {
            deposit.deposit(BigDecimal.ZERO, currency)
        }
        assertTrue(ex1.message!!.contains("Deposit must be > 0"))

        val ex2 = assertFailsWith<IllegalArgumentException> {
            deposit.deposit(BigDecimal("-1.00"), currency)
        }
        assertTrue(ex2.message!!.contains("Deposit must be > 0"))
    }

    @Test
    fun `deposit after close throws`() {
        val deposit = createDeposit(BigDecimal("200.00"))
        deposit.close()

        val e = assertFailsWith<DepositClosedException> {
            deposit.deposit(BigDecimal("10.00"), currency)
        }
        assertTrue(e.message!!.contains("Deposit is closed"))
    }

    @Test
    fun `getBalance after close throws`() {
        val deposit = createDeposit(BigDecimal("200.00"))
        deposit.close()

        val e = assertFailsWith<DepositClosedException> {
            deposit.getBalance()
        }
        assertTrue(e.message!!.contains("Deposit is closed"))
    }

    @Test
    fun `close twice throws`() {
        val deposit = createDeposit(BigDecimal("200.00"))
        deposit.close()

        val e = assertFailsWith<DepositClosedException> {
            deposit.close()
        }
        assertTrue(e.message!!.contains("Deposit is closed"))
    }
}