package `in`.gopalpoddar.textspur.features.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.gopalpoddar.textspur.features.auth.login.domain.repository.AuthRepository
import `in`.gopalpoddar.textspur.features.profile.domain.repository.UserRepository
import `in`.gopalpoddar.textspur.features.search.domain.usecase.CreateChatIdUseCase
import `in`.gopalpoddar.textspur.features.search.domain.usecase.InitializeChatUseCase
import `in`.gopalpoddar.textspur.features.search.domain.usecase.SearchUsersUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val searchUsersUseCase: SearchUsersUseCase,
    private val createChatIdUseCase: CreateChatIdUseCase,
    private val initializeChatUseCase: InitializeChatUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val _navigateToChatRoom = MutableSharedFlow<String>()
    val navigateToChatRoom = _navigateToChatRoom.asSharedFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query) }
        
        searchJob?.cancel()
        
        if (query.isBlank()) {
            _state.update { it.copy(users = emptyList(), isLoading = false, error = null) }
            return
        }

        searchJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            delay(500) // Debounce
            
            val currentUserId = authRepository.getCurrentUserId()
            
            searchUsersUseCase(query, currentUserId)
                .catch { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { users ->
                    _state.update { it.copy(users = users, isLoading = false, error = null) }
                }
        }
    }

    fun onUserClick(selectedUserId: String) {
        if (_state.value.isChatCreating) return

        viewModelScope.launch {
            _state.update { it.copy(isChatCreating = true, error = null) }
            
            val currentUserId = authRepository.getCurrentUserId()
            if (currentUserId == null) {
                _state.update { it.copy(isChatCreating = false, error = "User not logged in") }
                return@launch
            }

            try {
                val chatId = createChatIdUseCase(currentUserId, selectedUserId)
                
                // Initialize the chat participants before navigating
                val currentUserResult = userRepository.getCurrentUserProfile(currentUserId)
                val currentUserProfile = currentUserResult.getOrNull()
                val selectedUserProfile = _state.value.users.find { it.uid == selectedUserId }
                
                if (currentUserProfile != null && selectedUserProfile != null) {
                    initializeChatUseCase(
                        chatId = chatId,
                        currentUserId = currentUserId,
                        currentUserName = currentUserProfile.name,
                        otherUserId = selectedUserId,
                        otherUserName = selectedUserProfile.name
                    )
                }

                _navigateToChatRoom.emit(chatId)
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            } finally {
                _state.update { it.copy(isChatCreating = false) }
            }
        }
    }
}
