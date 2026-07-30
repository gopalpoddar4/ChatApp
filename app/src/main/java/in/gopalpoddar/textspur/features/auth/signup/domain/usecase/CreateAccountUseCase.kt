package `in`.gopalpoddar.textspur.features.auth.signup.domain.usecase

import `in`.gopalpoddar.textspur.features.auth.login.domain.repository.AuthRepository
import `in`.gopalpoddar.textspur.features.auth.signup.domain.model.CreateAccountResult
import `in`.gopalpoddar.textspur.features.profile.domain.usecase.CreateUserProfileUseCase
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val createUserProfileUseCase: CreateUserProfileUseCase
) {
    suspend operator fun invoke(name: String, email: String, password: String): CreateAccountResult {
        if (name.isBlank()) {
            return CreateAccountResult.AuthError(Exception("Name cannot be empty."))
        }
        if (email.isBlank()) {
            return CreateAccountResult.AuthError(Exception("Email cannot be empty."))
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return CreateAccountResult.AuthError(Exception("Invalid email format."))
        }
        if (password.isBlank()) {
            return CreateAccountResult.AuthError(Exception("Password cannot be empty."))
        }
        // Firebase Auth requires at least 6 characters for password
        if (password.length < 6) {
            return CreateAccountResult.AuthError(Exception("Password must be at least 6 characters long."))
        }
        
        val authResult = authRepository.signUp(name, email, password)
        return if (authResult.isSuccess) {
            val uid = authResult.getOrNull() ?: return CreateAccountResult.AuthError(Exception("UID is null after successful signup."))
            
            val profileResult = createUserProfileUseCase(uid = uid, name = name, email = email)
            if (profileResult.isSuccess) {
                CreateAccountResult.Success
            } else {
                CreateAccountResult.ProfileSaveFailed(uid, name, email)
            }
        } else {
            CreateAccountResult.AuthError(authResult.exceptionOrNull() ?: Exception("Unknown authentication error."))
        }
    }
}
