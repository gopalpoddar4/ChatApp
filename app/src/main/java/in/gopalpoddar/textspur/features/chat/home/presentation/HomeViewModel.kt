package `in`.gopalpoddar.textspur.features.chat.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.gopalpoddar.textspur.features.auth.login.domain.repository.AuthRepository
import `in`.gopalpoddar.textspur.features.chat.home.domain.usecase.GetChatsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getChatsUseCase: GetChatsUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        val currentUserId = authRepository.getCurrentUserId()
        _state.update { it.copy(currentUserId = currentUserId) }
        loadChats()
    }

    private fun loadChats() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getChatsUseCase()
                .catch { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { chats ->
                    _state.update { it.copy(chats = chats, isLoading = false, error = null) }
                }
        }
    }
}
