package `in`.gopalpoddar.textspur.features.profile.domain.manager

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PresenceManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val databaseReference: DatabaseReference
) {
    fun startPresence() {
        val currentUser = firebaseAuth.currentUser ?: return
        val uid = currentUser.uid
        val connectedRef = databaseReference.database.getReference(".info/connected")
        val userStatusRef = databaseReference.child("users").child(uid).child("isOnline")

        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    // When connection is established, set up onDisconnect
                    userStatusRef.onDisconnect().setValue(false).addOnCompleteListener {
                        // After setting up onDisconnect, update presence to true
                        userStatusRef.setValue(true)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Ignore or log error
            }
        })
    }
}
