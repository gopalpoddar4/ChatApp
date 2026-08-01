package `in`.gopalpoddar.textspur.features.chat.home.domain.model

import `in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile

data class Chat(
    val chatId: String = "",
    val participants: List<UserProfile> = emptyList(),
    val lastMessage: String = "",
    val lastMessageTime: Long = 0L,
    val unreadCount: Map<String, Int> = emptyMap(),
    val messages: List<Message> = emptyList()
)
