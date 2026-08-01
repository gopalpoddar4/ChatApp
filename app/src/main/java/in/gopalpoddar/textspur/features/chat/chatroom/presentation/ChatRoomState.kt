package `in`.gopalpoddar.textspur.features.chat.chatroom.presentation

import `in`.gopalpoddar.textspur.features.chat.home.domain.model.Message
import `in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile

data class ChatRoomState(
    val messages: List<Message> = emptyList(),
    val currentUserId: String? = null,
    val otherParticipant: UserProfile? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val messageText: String = ""
)
