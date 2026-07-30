package `in`.gopalpoddar.textspur.features.auth.login.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
}
