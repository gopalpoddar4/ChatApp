package `in`.gopalpoddar.textspur.features.auth.login.data.repository

import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
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

    override suspend fun signUp(name: String, email: String, password: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                dataSource.signUp(name, email, password)
                Result.success(Unit)
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is FirebaseAuthUserCollisionException -> "An account with this email already exists."
                    is FirebaseAuthWeakPasswordException -> "Password is too weak. Please use a stronger password."
                    is FirebaseAuthInvalidCredentialsException -> "Please enter a valid email address."
                    is FirebaseAuthException -> e.message ?: "Unable to create your account. Please try again."
                    else -> "An unexpected error occurred. Please check your connection and try again."
                }
                Result.failure(Exception(errorMessage))
            }
        }
    }
}
