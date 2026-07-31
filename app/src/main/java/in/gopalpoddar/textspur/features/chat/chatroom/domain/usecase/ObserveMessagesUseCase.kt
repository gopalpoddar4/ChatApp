package `in`.gopalpoddar.textspur.features.chat.chatroom.domain.usecase

import `in`.gopalpoddar.textspur.features.chat.home.domain.model.Message
import `in`.gopalpoddar.textspur.features.chat.home.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(chatId: String): Flow<List<Message>> {
        return repository.observeMessages(chatId)
    }
}
