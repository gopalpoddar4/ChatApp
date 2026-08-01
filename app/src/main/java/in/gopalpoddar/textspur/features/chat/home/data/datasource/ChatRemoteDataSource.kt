package `in`.gopalpoddar.textspur.features.chat.home.data.datasource

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import `in`.gopalpoddar.textspur.features.chat.home.domain.model.Chat
import `in`.gopalpoddar.textspur.features.chat.home.domain.model.Message
import `in`.gopalpoddar.textspur.features.chat.home.domain.model.Participant
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRemoteDataSource @Inject constructor(
    private val databaseReference: DatabaseReference
) {
    fun getChats(currentUserId: String): Flow<List<Chat>> = callbackFlow {
        val chatsRef = databaseReference.child("chats")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chats = mutableListOf<Chat>()
                for (chatSnapshot in snapshot.children) {
                    val participantsSnapshot = chatSnapshot.child("participants")
                    if (participantsSnapshot.hasChild(currentUserId)) {
                        val chatId = chatSnapshot.key ?: continue
                        
                        val participants = mutableListOf<`in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile>()
                        for (pSnapshot in participantsSnapshot.children) {
                            val uid = pSnapshot.key ?: continue
                            // Just storing the UID for now, details will be fetched by Repository
                            participants.add(`in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile(uid = uid))
                        }

                        val lastMessage = chatSnapshot.child("lastMessage").getValue(String::class.java) ?: ""
                        val lastMessageTime = chatSnapshot.child("lastMessageTime").getValue(Long::class.java) ?: 0L
                        
                        val unreadCount = mutableMapOf<String, Int>()
                        for (uSnapshot in chatSnapshot.child("unreadCount").children) {
                            val uid = uSnapshot.key ?: continue
                            val count = uSnapshot.getValue(Int::class.java) ?: 0
                            unreadCount[uid] = count
                        }

                        val messages = mutableListOf<Message>()
                        for (mSnapshot in chatSnapshot.child("messages").children) {
                            val id = mSnapshot.key ?: ""
                            val message = mSnapshot.child("message").getValue(String::class.java) ?: ""
                            val read = mSnapshot.child("read").getValue(Boolean::class.java) ?: mSnapshot.child("isRead").getValue(Boolean::class.java) ?: false
                            val receiverId = mSnapshot.child("receiverId").getValue(String::class.java) ?: ""
                            val senderId = mSnapshot.child("senderId").getValue(String::class.java) ?: ""
                            val timestamp = mSnapshot.child("timestamp").getValue(Long::class.java) ?: 0L
                            messages.add(Message(id, message, read, receiverId, senderId, timestamp))
                        }

                        chats.add(
                            Chat(
                                chatId = chatId,
                                participants = participants,
                                lastMessage = lastMessage,
                                lastMessageTime = lastMessageTime,
                                unreadCount = unreadCount,
                                messages = messages
                            )
                        )
                    }
                }
                chats.sortByDescending { it.lastMessageTime }
                trySend(chats)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        chatsRef.addValueEventListener(listener)
        awaitClose { chatsRef.removeEventListener(listener) }
    }

    fun observeMessages(chatId: String): Flow<List<Message>> = callbackFlow {
        val messagesRef = databaseReference.child("chats").child(chatId).child("messages")
        
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<Message>()
                for (mSnapshot in snapshot.children) {
                    val id = mSnapshot.key ?: continue
                    val message = mSnapshot.child("message").getValue(String::class.java) ?: ""
                    val read = mSnapshot.child("read").getValue(Boolean::class.java) ?: mSnapshot.child("isRead").getValue(Boolean::class.java) ?: false
                    val receiverId = mSnapshot.child("receiverId").getValue(String::class.java) ?: ""
                    val senderId = mSnapshot.child("senderId").getValue(String::class.java) ?: ""
                    val timestamp = mSnapshot.child("timestamp").getValue(Long::class.java) ?: 0L
                    messages.add(Message(id, message, read, receiverId, senderId, timestamp))
                }
                messages.sortBy { it.timestamp } // Chronological order
                trySend(messages)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        
        messagesRef.addValueEventListener(listener)
        awaitClose { messagesRef.removeEventListener(listener) }
    }

    suspend fun sendMessage(chatId: String, message: Message) {
        val messagesRef = databaseReference.child("chats").child(chatId).child("messages")
        val messageId = messagesRef.push().key ?: throw Exception("Failed to generate message ID")
        
        val messageWithId = message.copy(id = messageId)
        
        val updates = mapOf<String, Any>(
            "/chats/$chatId/messages/$messageId" to messageWithId,
            "/chats/$chatId/lastMessage" to message.message,
            "/chats/$chatId/lastMessageTime" to message.timestamp
        )
        
        databaseReference.updateChildren(updates).await()
    }



    suspend fun initializeChatParticipants(
        chatId: String,
        currentUserId: String,
        otherUserId: String
    ) {
        val updates = mapOf<String, Any>(
            "/chats/$chatId/participants/$currentUserId" to true,
            "/chats/$chatId/participants/$otherUserId" to true
        )
        databaseReference.updateChildren(updates).await()
    }
}
