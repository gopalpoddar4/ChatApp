package `in`.gopalpoddar.textspur.features.auth.login.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signUp(name: String, email: String, password: String) {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user
        
        user?.let {
            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }
            it.updateProfile(profileUpdates).await()
        }
    }
}
