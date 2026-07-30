package `in`.gopalpoddar.textspur.features.auth.signup.domain.model

sealed class CreateAccountResult {
    object Success : CreateAccountResult()
    data class ProfileSaveFailed(val uid: String, val name: String, val email: String) : CreateAccountResult()
    data class AuthError(val exception: Throwable) : CreateAccountResult()
}
