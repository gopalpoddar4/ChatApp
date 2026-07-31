package `in`.gopalpoddar.textspur.features.chat.chatroom.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.gopalpoddar.textspur.features.chat.chatroom.presentation.components.ChatInput
import `in`.gopalpoddar.textspur.features.chat.chatroom.presentation.components.ChatTopBar
import `in`.gopalpoddar.textspur.features.chat.chatroom.presentation.components.EmptyChatRoomState
import `in`.gopalpoddar.textspur.features.chat.chatroom.presentation.components.MessageBubble

@Composable
fun ChatRoomScreen(
    chatId: String,
    onNavigateBack: () -> Unit,
    viewModel: ChatRoomViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(chatId) {
        viewModel.initChat(chatId)
    }

    // Scroll to bottom when messages change
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            ChatTopBar(
                name = state.otherParticipant?.name ?: "Loading...",
                isOnline = state.otherParticipant?.isOnline ?: false,
                onNavigateBack = onNavigateBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (state.error != null) {
                    Text(
                        text = state.error!!,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (state.messages.isEmpty()) {
                    EmptyChatRoomState()
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.messages, key = { it.id }) { message ->
                            MessageBubble(
                                message = message,
                                isCurrentUser = message.senderId == state.currentUserId
                            )
                        }
                    }
                }
            }

            ChatInput(
                messageText = state.messageText,
                onMessageChange = viewModel::onMessageChange,
                onSendMessage = viewModel::sendMessage
            )
        }
    }
}
