package `in`.gopalpoddar.textspur.features.auth.login.domain.usecase

import `in`.gopalpoddar.textspur.features.auth.login.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        if (email.isBlank()) {
            return Result.failure(Exception("Email cannot be empty."))
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.failure(Exception("Invalid email format."))
        }
        if (password.isBlank()) {
            return Result.failure(Exception("Password cannot be empty."))
        }
        return authRepository.login(email, password)
    }
}
