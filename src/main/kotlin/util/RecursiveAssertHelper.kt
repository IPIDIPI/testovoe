package util

import org.assertj.core.api.Assertions.assertThat


//заглушка что бы не настраивать точные компейры
object RecursiveAssertHelper {
    fun <T> assertEqual(actual: T, expected: T) {
        try {
            assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected)
        } catch (e: AssertionError) {
            val msg = e.message ?: throw e
            val cleaned = msg.substringBefore(
                "The recursive comparison was performed with this configuration:"
            ).trimEnd()
            throw AssertionError(cleaned)
        }
    }
}
