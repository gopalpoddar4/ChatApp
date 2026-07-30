package `in`.gopalpoddar.textspur.features.auth.login.domain.repository

interface AuthRepository {
    fun isUserAuthenticated(): Boolean
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun signUp(name: String, email: String, password: String): Result<String>
}
