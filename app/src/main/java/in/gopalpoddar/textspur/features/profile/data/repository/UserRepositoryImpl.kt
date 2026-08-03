package `in`.gopalpoddar.textspur.features.profile.data.repository

import `in`.gopalpoddar.textspur.core.database.AppDatabase
import `in`.gopalpoddar.textspur.core.database.dao.UserDao
import `in`.gopalpoddar.textspur.core.database.entity.UserEntity
import `in`.gopalpoddar.textspur.features.profile.data.datasource.UserLocalDataSource
import `in`.gopalpoddar.textspur.features.profile.data.datasource.UserRemoteDataSource
import `in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile
import `in`.gopalpoddar.textspur.features.profile.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localDataSource: UserLocalDataSource, // Keeping datastore just in case it's used elsewhere, but will primarily use Room
    private val remoteDataSource: UserRemoteDataSource,
    private val userDao: UserDao,
    private val appDatabase: AppDatabase
) : UserRepository {

    override suspend fun saveUserProfile(userProfile: UserProfile): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                remoteDataSource.saveUserProfile(userProfile)
                localDataSource.saveUserProfile(userProfile)
                userDao.upsertUser(
                    UserEntity(
                        uid = userProfile.uid,
                        name = userProfile.name,
                        email = userProfile.email,
                        username = userProfile.username,
                        isOnline = userProfile.isOnline,
                        isVerified = userProfile.isVerified
                    )
                )
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
                // First try Room
                val roomProfile = userDao.getUserById(uid)
                if (roomProfile != null) {
                    return@withContext Result.success(
                        UserProfile(
                            uid = roomProfile.uid,
                            name = roomProfile.name,
                            email = roomProfile.email,
                            username = roomProfile.username,
                            isOnline = roomProfile.isOnline,
                            isVerified = roomProfile.isVerified
                        )
                    )
                }

                // Fallback to DataStore
                val localProfile = localDataSource.getUserProfile()
                if (localProfile != null && localProfile.uid == uid) {
                    // Update Room since it was missing
                    userDao.upsertUser(
                        UserEntity(
                            uid = localProfile.uid,
                            name = localProfile.name,
                            email = localProfile.email,
                            username = localProfile.username,
                            isOnline = localProfile.isOnline,
                            isVerified = localProfile.isVerified
                        )
                    )
                    return@withContext Result.success(localProfile)
                }

                // Fallback to Remote
                val remoteProfile = remoteDataSource.getUserProfile(uid)
                if (remoteProfile != null) {
                    localDataSource.saveUserProfile(remoteProfile)
                    userDao.upsertUser(
                        UserEntity(
                            uid = remoteProfile.uid,
                            name = remoteProfile.name,
                            email = remoteProfile.email,
                            username = remoteProfile.username,
                            isOnline = remoteProfile.isOnline,
                            isVerified = remoteProfile.isVerified
                        )
                    )
                    Result.success(remoteProfile)
                } else {
                    Result.failure(Exception("User profile not found in remote database."))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun deleteUserProfile(uid: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                remoteDataSource.deleteUserProfile(uid)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun clearLocalUserData() {
        withContext(Dispatchers.IO) {
            localDataSource.clearUserProfile()
            appDatabase.clearAllTables()
        }
    }
}
