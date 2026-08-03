package `in`.gopalpoddar.textspur.features.profile.domain.usecase

import `in`.gopalpoddar.textspur.features.auth.login.domain.repository.AuthRepository
import `in`.gopalpoddar.textspur.features.profile.domain.repository.UserRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        val uid = authRepository.getCurrentUserId() ?: return Result.failure(Exception("Not logged in"))
        
        // STEP 1: Delete Realtime DB profile
        val dbResult = userRepository.deleteUserProfile(uid)
        if (dbResult.isFailure) return dbResult
        
        // STEP 2: Delete Auth Account
        val authResult = authRepository.deleteAccount()
        if (authResult.isFailure) return authResult
        
        // STEP 3: Clear local data
        userRepository.clearLocalUserData()
        
        return Result.success(Unit)
    }
}
