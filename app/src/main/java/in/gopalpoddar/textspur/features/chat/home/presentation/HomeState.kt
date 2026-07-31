package `in`.gopalpoddar.textspur.features.chat.home.presentation

import `in`.gopalpoddar.textspur.features.chat.home.domain.model.Chat

data class HomeState(
    val chats: List<Chat> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val currentUserId: String? = null
)
