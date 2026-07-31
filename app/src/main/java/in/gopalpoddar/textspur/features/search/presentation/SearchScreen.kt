package `in`.gopalpoddar.textspur.features.search.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import `in`.gopalpoddar.textspur.features.search.presentation.components.SearchResultItem
import `in`.gopalpoddar.textspur.features.search.presentation.components.SearchTopBar
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToChatRoom: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigateToChatRoom.collectLatest { chatId ->
            onNavigateToChatRoom(chatId)
        }
    }

    Scaffold(
        topBar = {
            SearchTopBar(
                query = state.query,
                onQueryChange = viewModel::onQueryChange,
                onNavigateBack = onNavigateBack
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                if (state.query.isBlank()) {
                    // Initial empty state
                } else if (state.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (state.error != null && !state.isChatCreating) { // Don't show search error if chat creation failed, let the overlay handle it
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Error: ${state.error}", color = Color.Red)
                    }
                } else if (state.users.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No users found",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.users, key = { it.uid }) { user ->
                            SearchResultItem(
                                user = user,
                                onClick = { viewModel.onUserClick(user.uid) }
                            )
                        }
                    }
                }
            }

            // Loading overlay for chat creation
            if (state.isChatCreating) {
                Box(
                    modifier = Modifier.fillMaxSize(), // Optionally add a translucent background here
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            // Error overlay for chat creation
            if (state.isChatCreating.not() && state.error != null && state.query.isNotBlank() && !state.isLoading) {
                // If it's a general error, it might be displayed here. 
                // Currently relying on the main error block above.
            }
        }
    }
}
