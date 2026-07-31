package `in`.gopalpoddar.textspur.features.chat.home.presentation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import `in`.gopalpoddar.textspur.features.chat.home.domain.model.Chat

@Composable
fun ChatList(
    chats: List<Chat>,
    currentUserId: String,
    onChatClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(chats, key = { it.chatId }) { chat ->
            ChatListItem(
                chat = chat,
                currentUserId = currentUserId,
                onClick = { onChatClick(chat.chatId) }
            )
        }
    }
}
