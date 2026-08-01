package `in`.gopalpoddar.textspur.core.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import `in`.gopalpoddar.textspur.core.database.entity.ChatEntity
import `in`.gopalpoddar.textspur.core.database.entity.ChatParticipantCrossRef
import `in`.gopalpoddar.textspur.core.database.entity.UserEntity

data class ChatWithParticipants(
    @Embedded val chat: ChatEntity,
    @Relation(
        parentColumn = "chatId",
        entityColumn = "uid",
        associateBy = Junction(ChatParticipantCrossRef::class)
    )
    val participants: List<UserEntity>
)
