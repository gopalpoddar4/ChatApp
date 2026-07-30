package `in`.gopalpoddar.textspur.features.profile.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import `in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile
import `in`.gopalpoddar.textspur.features.profile.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth
) {
    suspend operator fun invoke(): Result<UserProfile> {
        val uid = firebaseAuth.currentUser?.uid 
            ?: return Result.failure(Exception("User is not authenticated."))
        return userRepository.getCurrentUserProfile(uid)
    }
}
