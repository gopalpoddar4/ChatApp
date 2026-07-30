package `in`.gopalpoddar.textspur.features.auth.login.presentation

data class LoginState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
