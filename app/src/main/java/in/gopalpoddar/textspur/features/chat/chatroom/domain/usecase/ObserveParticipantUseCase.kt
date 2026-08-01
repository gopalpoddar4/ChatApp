package `in`.gopalpoddar.textspur.features.chat.chatroom.domain.usecase

import `in`.gopalpoddar.textspur.features.chat.home.domain.repository.ChatRepository
import `in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveParticipantUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(chatId: String, participantId: String): Flow<UserProfile?> {
        return repository.observeParticipant(chatId, participantId)
    }
}
