package `in`.gopalpoddar.textspur.features.profile.domain.manager

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PresenceManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val databaseReference: DatabaseReference
) : DefaultLifecycleObserver {

    private var presenceListener: ValueEventListener? = null

    fun startPresence() {
        // Now mostly handled by lifecycle, but we can set up any one-time things here if needed
    }

    fun stopPresence() {
        val uid = firebaseAuth.currentUser?.uid ?: return
        val userStatusRef = databaseReference.child("users").child(uid).child("isOnline")
        userStatusRef.setValue(false)
        
        val connectedRef = databaseReference.database.getReference(".info/connected")
        presenceListener?.let { connectedRef.removeEventListener(it) }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        val currentUser = firebaseAuth.currentUser ?: return
        val uid = currentUser.uid
        val connectedRef = databaseReference.database.getReference(".info/connected")
        val userStatusRef = databaseReference.child("users").child(uid).child("isOnline")

        presenceListener = object : ValueEventListener {
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
        }
        connectedRef.addValueEventListener(presenceListener!!)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        val currentUser = firebaseAuth.currentUser ?: return
        val uid = currentUser.uid
        val userStatusRef = databaseReference.child("users").child(uid).child("isOnline")
        
        // Explicitly set isOnline to false when app goes to background
        userStatusRef.setValue(false)
        
        // Optionally cancel the onDisconnect operation if desired, 
        // but it's safe to leave it in case the app is killed while in background
    }
}
