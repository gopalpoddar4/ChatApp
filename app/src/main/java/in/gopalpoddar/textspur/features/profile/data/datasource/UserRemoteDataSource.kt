package `in`.gopalpoddar.textspur.features.profile.data.datasource

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import `in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    suspend fun deleteUserProfile(uid: String) {
        kotlinx.coroutines.withTimeout(10000.milliseconds) {
            databaseReference
                .child("users")
                .child(uid)
                .removeValue()
                .await()
        }
    }

    suspend fun getUserProfile(uid: String): UserProfile? {
        val snapshot = databaseReference.child("users").child(uid).get().await()
        return snapshot.getValue(UserProfile::class.java)
    }

    fun observeUserProfile(uid: String): Flow<UserProfile?> = callbackFlow {
        val userRef = databaseReference.child("users").child(uid)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(UserProfile::class.java))
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        userRef.addValueEventListener(listener)
        awaitClose { userRef.removeEventListener(listener) }
    }
}
