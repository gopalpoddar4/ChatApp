package `in`.gopalpoddar.textspur.features.chat.home.domain.repository

import `in`.gopalpoddar.textspur.features.chat.home.domain.model.Chat
import `in`.gopalpoddar.textspur.features.chat.home.domain.model.Message
import `in`.gopalpoddar.textspur.features.chat.home.domain.model.Participant
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChats(currentUserId: String): Flow<List<Chat>>
    fun observeMessages(chatId: String): Flow<List<Message>>
    suspend fun sendMessage(chatId: String, message: Message): Result<Unit>
    suspend fun initializeChatParticipants(chatId: String, currentUserId: String, otherUserId: String): Result<Unit>
    fun observeParticipant(chatId: String, participantId: String): Flow<`in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile?>
}
