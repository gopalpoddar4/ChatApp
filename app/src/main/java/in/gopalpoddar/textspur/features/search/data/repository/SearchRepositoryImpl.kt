package `in`.gopalpoddar.textspur.features.search.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import `in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile
import `in`.gopalpoddar.textspur.features.search.domain.repository.SearchRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference
) : SearchRepository {

    override fun searchUsersByUsername(query: String): Flow<List<UserProfile>> = callbackFlow {
        if (query.isBlank()) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val usersRef = databaseReference.child("users")
        val searchQuery = usersRef.orderByChild("username")
            .startAt(query)
            .endAt(query + "\uf8ff")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<UserProfile>()
                for (userSnapshot in snapshot.children) {
                    val uid = userSnapshot.key ?: continue
                    val name = userSnapshot.child("name").getValue(String::class.java) ?: ""
                    val email = userSnapshot.child("email").getValue(String::class.java) ?: ""
                    val username = userSnapshot.child("username").getValue(String::class.java) ?: ""
                    
                    users.add(UserProfile(uid = uid, name = name, email = email, username = username))
                }
                trySend(users)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        searchQuery.addListenerForSingleValueEvent(listener) // For search we usually just want a single snapshot per query update, but we can also use addValueEventListener if real-time search is desired. Let's use single value event since the viewmodel debounces and triggers a new flow per query.
        
        awaitClose { 
            // In case of single value event, removal is not strictly required but good practice if cancelled early
            searchQuery.removeEventListener(listener) 
        }
    }
}
