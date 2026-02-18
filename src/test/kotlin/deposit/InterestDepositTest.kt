package deposit

import dhapr.exception.DepositClosedException
import dhapr.product.deposit.model.InterestDeposit
import dhapr.product.deposit.strategy.balace.RefillableDepositStrategy
import dhapr.product.deposit.strategy.close.InterestCloseStrategy
import dhapr.product.policy.currency.MultiCurrencyPolicy
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class InterestDepositTest : AbstractDepositTest() {

    override val currency: Currency = Currency.getInstance("USD")

    override fun createDeposit(initial: BigDecimal): InterestDeposit =
        InterestDeposit(
            name = "InterestDeposit",
            currency = currency,
            initialBalance = initial,
            balanceStrategy = RefillableDepositStrategy,
            currencyPolicy = MultiCurrencyPolicy,
            closeStrategy = InterestCloseStrategy(BigDecimal("0.10"))
        )


    @Test
    fun `close applies interest`() {
        val deposit = createDeposit(BigDecimal("100.00"))
        val result = deposit.close()
        assertEquals(BigDecimal("110.00"), result)
    }

    @Test
    fun `close marks deposit closed`() {
        val deposit = createDeposit(BigDecimal("100.00"))
        deposit.close()

        val e = assertFailsWith<DepositClosedException> {
            deposit.getBalance()
        }
        assertTrue(e.message!!.contains("closed"))
    }

}
