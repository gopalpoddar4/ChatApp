package `in`.gopalpoddar.textspur.features.auth.login.data.repository

import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import `in`.gopalpoddar.textspur.features.auth.login.data.datasource.FirebaseAuthDataSource
import `in`.gopalpoddar.textspur.features.auth.login.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val dataSource: FirebaseAuthDataSource
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                dataSource.login(email, password)
                Result.success(Unit)
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is FirebaseAuthInvalidUserException -> "No account found with this email."
                    is FirebaseAuthInvalidCredentialsException -> "Invalid email or password."
                    is FirebaseAuthException -> e.message ?: "Authentication failed."
                    else -> "An unexpected error occurred."
                }
                Result.failure(Exception(errorMessage))
            }
        }
    }
}
