package `in`.gopalpoddar.textspur.features.profile.data.repository

import `in`.gopalpoddar.textspur.features.profile.data.datasource.UserLocalDataSource
import `in`.gopalpoddar.textspur.features.profile.data.datasource.UserRemoteDataSource
import `in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile
import `in`.gopalpoddar.textspur.features.profile.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun saveUserProfile(userProfile: UserProfile): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                remoteDataSource.saveUserProfile(userProfile)
                localDataSource.saveUserProfile(userProfile)
                Result.success(Unit)
            } catch (e: Exception) {
                android.util.Log.e("UserRepositoryImpl", "User profile database write failed: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    override suspend fun getCurrentUserProfile(uid: String): Result<UserProfile> {
        return withContext(Dispatchers.IO) {
            try {
                val localProfile = localDataSource.getUserProfile()
                if (localProfile != null && localProfile.uid == uid) {
                    return@withContext Result.success(localProfile)
                }

                val remoteProfile = remoteDataSource.getUserProfile(uid)
                if (remoteProfile != null) {
                    localDataSource.saveUserProfile(remoteProfile)
                    Result.success(remoteProfile)
                } else {
                    Result.failure(Exception("User profile not found in remote database."))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
