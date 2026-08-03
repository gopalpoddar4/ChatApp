package `in`.gopalpoddar.textspur.features.profile.domain.usecase

import `in`.gopalpoddar.textspur.features.auth.login.domain.repository.AuthRepository
import `in`.gopalpoddar.textspur.features.profile.domain.repository.UserRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        val logoutResult = authRepository.logout()
        if (logoutResult.isSuccess) {
            userRepository.clearLocalUserData()
        }
        return logoutResult
    }
}
