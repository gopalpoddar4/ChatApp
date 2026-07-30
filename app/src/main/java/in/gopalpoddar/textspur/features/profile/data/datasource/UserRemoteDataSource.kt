package `in`.gopalpoddar.textspur.features.profile.data.datasource

import com.google.firebase.database.DatabaseReference
import `in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class UserRemoteDataSource @Inject constructor(
    private val databaseReference: DatabaseReference
) {
    suspend fun saveUserProfile(userProfile: UserProfile) {
        kotlinx.coroutines.withTimeout(10000.milliseconds) {
            databaseReference
                .child("users")
                .child(userProfile.uid)
                .setValue(userProfile)
                .await()
        }
    }

    suspend fun getUserProfile(uid: String): UserProfile? {
        val snapshot = databaseReference.child("users").child(uid).get().await()
        return snapshot.getValue(UserProfile::class.java)
    }
}
