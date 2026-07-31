package `in`.gopalpoddar.textspur.features.chat.home.domain.model

data class Chat(
    val chatId: String = "",
    val participants: Map<String, Participant> = emptyMap(),
    val lastMessage: String = "",
    val lastMessageTime: Long = 0L,
    val unreadCount: Map<String, Int> = emptyMap(),
    val messages: List<Message> = emptyList()
)
