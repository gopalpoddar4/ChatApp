package `in`.gopalpoddar.textspur.core.database.entity

import androidx.room.Entity

@Entity(primaryKeys = ["chatId", "uid"])
data class ChatParticipantCrossRef(
    val chatId: String,
    val uid: String,
    val unreadCount: Int = 0
)
