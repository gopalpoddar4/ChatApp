package `in`.gopalpoddar.textspur.features.search.domain.usecase

import `in`.gopalpoddar.textspur.features.chat.home.domain.repository.ChatRepository
import javax.inject.Inject

class InitializeChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        chatId: String,
        currentUserId: String,
        currentUserName: String,
        otherUserId: String,
        otherUserName: String
    ): Result<Unit> {
        return chatRepository.initializeChatParticipants(
            chatId = chatId,
            currentUserId = currentUserId,
            currentUserName = currentUserName,
            otherUserId = otherUserId,
            otherUserName = otherUserName
        )
    }
}
