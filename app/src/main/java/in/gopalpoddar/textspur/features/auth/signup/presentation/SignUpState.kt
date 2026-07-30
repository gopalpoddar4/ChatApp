package `in`.gopalpoddar.textspur.features.auth.signup.presentation

data class SignUpState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isProfileSaveRequired: Boolean = false,
    val error: String? = null
)
