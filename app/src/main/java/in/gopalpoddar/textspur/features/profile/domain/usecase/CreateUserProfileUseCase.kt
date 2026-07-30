package `in`.gopalpoddar.textspur.features.profile.domain.usecase

import `in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile
import `in`.gopalpoddar.textspur.features.profile.domain.repository.UserRepository
import javax.inject.Inject

class CreateUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(uid: String, name: String, email: String): Result<Unit> {
        val username = email.substringBefore("@")
        val userProfile = UserProfile(
            uid = uid,
            name = name,
            email = email,
            username = username
        )
        return userRepository.saveUserProfile(userProfile)
    }
}
