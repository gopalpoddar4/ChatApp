package `in`.gopalpoddar.textspur.features.search.domain.usecase

import `in`.gopalpoddar.textspur.features.chat.home.domain.repository.ChatRepository
import javax.inject.Inject

class InitializeChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        chatId: String,
        currentUserId: String,
        otherUserId: String
    ): Result<Unit> {
        return chatRepository.initializeChatParticipants(
            chatId = chatId,
            currentUserId = currentUserId,
            otherUserId = otherUserId
        )
    }
}
