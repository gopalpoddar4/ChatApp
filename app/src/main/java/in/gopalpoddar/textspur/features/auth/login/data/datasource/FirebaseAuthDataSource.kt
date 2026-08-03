package `in`.gopalpoddar.textspur.features.auth.login.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    fun isUserAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    suspend fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signUp(name: String, email: String, password: String): String {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: throw Exception("User creation failed, user is null")
        
        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }
        user.updateProfile(profileUpdates).await()
        
        return user.uid
    }

    suspend fun logout() {
        firebaseAuth.signOut()
    }

    suspend fun reauthenticate(password: String) {
        val user = firebaseAuth.currentUser ?: throw Exception("User not logged in")
        val email = user.email ?: throw Exception("User email not found")
        val credential = com.google.firebase.auth.EmailAuthProvider.getCredential(email, password)
        user.reauthenticate(credential).await()
    }

    suspend fun deleteAccount() {
        val user = firebaseAuth.currentUser ?: throw Exception("User not logged in")
        user.delete().await()
    }
}
