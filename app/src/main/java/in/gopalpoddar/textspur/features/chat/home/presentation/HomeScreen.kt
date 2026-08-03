package `in`.gopalpoddar.textspur.features.chat.home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.gopalpoddar.textspur.features.chat.home.presentation.components.ChatList
import `in`.gopalpoddar.textspur.features.chat.home.presentation.components.ChatSearchBar
import `in`.gopalpoddar.textspur.features.chat.home.presentation.components.EmptyChatState
import `in`.gopalpoddar.textspur.features.chat.home.presentation.components.HomeTopBar
import `in`.gopalpoddar.textspur.features.chat.home.presentation.components.NewChatFab

@Composable
fun HomeScreen(
    onNavigateToChatRoom: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSearch: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            HomeTopBar({
                onNavigateToProfile()
            })
        },
        floatingActionButton = {
            NewChatFab(
                onClick = onNavigateToSearch
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                ChatSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it }
                )
                
                if (state.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (state.error != null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Error: ${state.error}", color = Color.Red)
                    }
                } else {
                    val filteredChats = if (searchQuery.isBlank()) {
                        state.chats
                    } else {
                        state.chats.filter { chat ->
                            val otherParticipant = chat.participants.firstOrNull { it.uid != state.currentUserId }
                            val name = otherParticipant?.name ?: ""
                            name.contains(searchQuery, ignoreCase = true)
                        }
                    }

                    if (filteredChats.isEmpty()) {
                        EmptyChatState()
                    } else {
                        state.currentUserId?.let { uid ->
                            ChatList(
                                chats = filteredChats,
                                currentUserId = uid,
                                onChatClick = onNavigateToChatRoom
                            )
                        }
                    }
                }
            }
        }
    }
}
