package `in`.gopalpoddar.textspur.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val chatId: String,
    val lastMessage: String,
    val lastMessageTime: Long
)
