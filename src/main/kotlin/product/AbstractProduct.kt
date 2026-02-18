package dhapr.product

abstract class AbstractProduct(
    val name: String
) {
    init {
        require(name.isNotBlank()) { "Name cannot be blank" }
    }
}