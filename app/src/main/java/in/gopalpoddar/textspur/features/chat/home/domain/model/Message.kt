package `in`.gopalpoddar.textspur.features.chat.home.domain.model

data class Message(
    val id: String = "",
    val message: String = "",
    val read: Boolean = false,
    val receiverId: String = "",
    val senderId: String = "",
    val timestamp: Long = 0L
)
