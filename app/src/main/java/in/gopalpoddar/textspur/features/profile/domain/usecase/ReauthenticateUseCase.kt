package `in`.gopalpoddar.textspur.features.profile.domain.usecase

import `in`.gopalpoddar.textspur.features.auth.login.domain.repository.AuthRepository
import javax.inject.Inject

class ReauthenticateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(password: String): Result<Unit> {
        return authRepository.reauthenticate(password)
    }
}
