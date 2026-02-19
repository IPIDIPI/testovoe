package models.current

data class ErrorResponse(
    val error: Error
)

data class Error(
    val code: Int,
    val message: String
)