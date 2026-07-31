package `in`.gopalpoddar.textspur.features.chat.chatroom.domain.usecase

import `in`.gopalpoddar.textspur.features.chat.home.domain.model.Message
import `in`.gopalpoddar.textspur.features.chat.home.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatId: String, message: Message): Result<Unit> {
        if (message.message.isBlank()) {
            return Result.failure(Exception("Message cannot be empty"))
        }
        return repository.sendMessage(chatId, message)
    }
}
