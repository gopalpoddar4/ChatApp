package `in`.gopalpoddar.textspur.features.profile.domain.repository

import `in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile

interface UserRepository {
    suspend fun saveUserProfile(userProfile: UserProfile): Result<Unit>
    suspend fun getCurrentUserProfile(uid: String): Result<UserProfile>
    suspend fun deleteUserProfile(uid: String): Result<Unit>
    suspend fun clearLocalUserData()
}
