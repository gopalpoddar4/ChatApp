package `in`.gopalpoddar.textspur.features.auth.login.domain.repository

interface AuthRepository {
    fun isUserAuthenticated(): Boolean
    fun getCurrentUserId(): String?
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun signUp(name: String, email: String, password: String): Result<String>
}
