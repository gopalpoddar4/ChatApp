package `in`.gopalpoddar.textspur.features.chat.home.domain.usecase

import `in`.gopalpoddar.textspur.features.auth.login.domain.repository.AuthRepository
import `in`.gopalpoddar.textspur.features.chat.home.domain.model.Chat
import `in`.gopalpoddar.textspur.features.chat.home.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class GetChatsUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<List<Chat>> {
        val currentUserId = authRepository.getCurrentUserId()
        return if (currentUserId != null) {
            chatRepository.getChats(currentUserId)
        } else {
            emptyFlow()
        }
    }
}
