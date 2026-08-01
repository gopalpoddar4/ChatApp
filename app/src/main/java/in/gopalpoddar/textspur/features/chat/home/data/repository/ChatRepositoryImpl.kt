package `in`.gopalpoddar.textspur.features.chat.home.data.repository

import `in`.gopalpoddar.textspur.core.database.dao.ChatDao
import `in`.gopalpoddar.textspur.core.database.dao.UserDao
import `in`.gopalpoddar.textspur.core.database.entity.ChatEntity
import `in`.gopalpoddar.textspur.core.database.entity.ChatParticipantCrossRef
import `in`.gopalpoddar.textspur.core.database.entity.UserEntity
import `in`.gopalpoddar.textspur.features.chat.home.data.datasource.ChatRemoteDataSource
import `in`.gopalpoddar.textspur.features.chat.home.domain.model.Chat
import `in`.gopalpoddar.textspur.features.chat.home.domain.model.Message
import `in`.gopalpoddar.textspur.features.chat.home.domain.repository.ChatRepository
import `in`.gopalpoddar.textspur.features.profile.data.datasource.UserRemoteDataSource
import `in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val remoteDataSource: ChatRemoteDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val chatDao: ChatDao,
    private val userDao: UserDao
) : ChatRepository {

    override fun getChats(currentUserId: String): Flow<List<Chat>> {
        // 1. Start a background sync
        CoroutineScope(Dispatchers.IO).launch {
            syncChats(currentUserId)
        }

        // 2. Return data from Room (Single Source of Truth for UI)
        return chatDao.observeChatsForUser(currentUserId).map { chatWithParticipantsList ->
            chatWithParticipantsList.map { chatWithParticipants ->
                val unreadMap = mutableMapOf<String, Int>()
                chatWithParticipants.participants.forEach { userEntity ->
                    // Actually we need the cross ref to get unread count accurately. For now, defaulting to 0 or querying it if needed.
                    // This can be optimized later if unreadCount becomes a priority.
                    val count = chatDao.getUnreadCount(chatWithParticipants.chat.chatId, userEntity.uid) ?: 0
                    unreadMap[userEntity.uid] = count
                }

                Chat(
                    chatId = chatWithParticipants.chat.chatId,
                    lastMessage = chatWithParticipants.chat.lastMessage,
                    lastMessageTime = chatWithParticipants.chat.lastMessageTime,
                    unreadCount = unreadMap,
                    participants = chatWithParticipants.participants.map {
                        UserProfile(
                            uid = it.uid,
                            name = it.name,
                            email = it.email,
                            username = it.username,
                            isOnline = it.isOnline,
                            isVerified = it.isVerified
                        )
                    }
                )
            }
        }
    }

    private suspend fun syncChats(currentUserId: String) {
        try {
            // Collect flow just once to sync
            remoteDataSource.getChats(currentUserId).collect { remoteChats ->
                val userIdsToFetch = mutableSetOf<String>()
                
                val chatEntities = mutableListOf<ChatEntity>()
                val crossRefs = mutableListOf<ChatParticipantCrossRef>()

                for (chat in remoteChats) {
                    chatEntities.add(
                        ChatEntity(
                            chatId = chat.chatId,
                            lastMessage = chat.lastMessage,
                            lastMessageTime = chat.lastMessageTime
                        )
                    )

                    // Note: In the new structure, ChatRemoteDataSource should return participants as just UIDs (with dummy UserProfiles).
                    // Wait, currently ChatRemoteDataSource still parses participants in getChats. We'll update it.
                    for (participant in chat.participants) {
                        userIdsToFetch.add(participant.uid)
                        crossRefs.add(
                            ChatParticipantCrossRef(
                                chatId = chat.chatId,
                                uid = participant.uid,
                                unreadCount = chat.unreadCount[participant.uid] ?: 0
                            )
                        )
                    }
                }

                // Fetch missing users concurrently
                val userEntities = userIdsToFetch.map { uid ->
                    CoroutineScope(Dispatchers.IO).async {
                        val profile = userRemoteDataSource.getUserProfile(uid)
                        profile?.let {
                            UserEntity(
                                uid = it.uid,
                                name = it.name,
                                email = it.email,
                                username = it.username,
                                isOnline = it.isOnline,
                                isVerified = it.isVerified
                            )
                        }
                    }
                }.awaitAll().filterNotNull()

                // Save to Room
                userDao.upsertUsers(userEntities)
                chatDao.upsertChats(chatEntities)
                chatDao.upsertChatParticipants(crossRefs)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    override fun observeParticipant(chatId: String, participantId: String): Flow<UserProfile?> {
        // Observe participant directly from Room's user table
        return userDao.observeUserById(participantId).map { userEntity ->
            userEntity?.let {
                UserProfile(
                    uid = it.uid,
                    name = it.name,
                    email = it.email,
                    username = it.username,
                    isOnline = it.isOnline,
                    isVerified = it.isVerified
                )
            }
        }
    }

    override suspend fun initializeChatParticipants(
        chatId: String,
        currentUserId: String,
        otherUserId: String
    ): Result<Unit> {
        return try {
            remoteDataSource.initializeChatParticipants(
                chatId, currentUserId, otherUserId
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
