package `in`.gopalpoddar.textspur.features.auth.signup.domain.usecase

import `in`.gopalpoddar.textspur.features.auth.login.domain.repository.AuthRepository
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<Unit> {
        if (name.isBlank()) {
            return Result.failure(Exception("Name cannot be empty."))
        }
        if (email.isBlank()) {
            return Result.failure(Exception("Email cannot be empty."))
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.failure(Exception("Invalid email format."))
        }
        if (password.isBlank()) {
            return Result.failure(Exception("Password cannot be empty."))
        }
        // Firebase Auth requires at least 6 characters for password
        if (password.length < 6) {
            return Result.failure(Exception("Password must be at least 6 characters long."))
        }
        return authRepository.signUp(name, email, password)
    }
}
