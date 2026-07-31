package `in`.gopalpoddar.textspur.features.chat.home.data.repository

import `in`.gopalpoddar.textspur.features.chat.home.data.datasource.ChatRemoteDataSource
import `in`.gopalpoddar.textspur.features.chat.home.domain.model.Chat
import `in`.gopalpoddar.textspur.features.chat.home.domain.repository.ChatRepository
import `in`.gopalpoddar.textspur.features.chat.home.domain.model.Message
import `in`.gopalpoddar.textspur.features.chat.home.domain.model.Participant
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val remoteDataSource: ChatRemoteDataSource
) : ChatRepository {
    override fun getChats(currentUserId: String): Flow<List<Chat>> {
        return remoteDataSource.getChats(currentUserId)
    }

    override fun observeMessages(chatId: String): Flow<List<Message>> {
        return remoteDataSource.observeMessages(chatId)
    }

    override suspend fun sendMessage(chatId: String, message: Message): Result<Unit> {
        return try {
            remoteDataSource.sendMessage(chatId, message)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeParticipant(chatId: String, participantId: String): Flow<Participant?> {
        return remoteDataSource.observeParticipant(chatId, participantId)
    }

    override suspend fun initializeChatParticipants(
        chatId: String,
        currentUserId: String,
        currentUserName: String,
        otherUserId: String,
        otherUserName: String
    ): Result<Unit> {
        return try {
            remoteDataSource.initializeChatParticipants(
                chatId, currentUserId, currentUserName, otherUserId, otherUserName
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
