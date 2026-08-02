package `in`.gopalpoddar.textspur.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import `in`.gopalpoddar.textspur.core.database.entity.ChatEntity
import `in`.gopalpoddar.textspur.core.database.entity.ChatParticipantCrossRef
import `in`.gopalpoddar.textspur.core.database.model.ChatWithParticipants
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Upsert
    suspend fun upsertChat(chat: ChatEntity)

    @Upsert
    suspend fun upsertChats(chats: List<ChatEntity>)

    @Upsert
    suspend fun upsertChatParticipant(crossRef: ChatParticipantCrossRef)

    @Upsert
    suspend fun upsertChatParticipants(crossRefs: List<ChatParticipantCrossRef>)

    @Transaction
    @Query("""
        SELECT chats.* FROM chats 
        INNER JOIN ChatParticipantCrossRef ON chats.chatId = ChatParticipantCrossRef.chatId
        LEFT JOIN users ON users.uid = ChatParticipantCrossRef.uid
        WHERE ChatParticipantCrossRef.uid = :userId
        ORDER BY chats.lastMessageTime DESC
    """)
    fun observeChatsForUser(userId: String): Flow<List<ChatWithParticipants>>
    
    @Query("SELECT unreadCount FROM ChatParticipantCrossRef WHERE chatId = :chatId AND uid = :uid")
    suspend fun getUnreadCount(chatId: String, uid: String): Int?
}
