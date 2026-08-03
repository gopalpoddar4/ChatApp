package `in`.gopalpoddar.textspur.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.gopalpoddar.textspur.features.auth.login.domain.repository.AuthRepository
import `in`.gopalpoddar.textspur.features.profile.domain.usecase.DeleteAccountUseCase
import `in`.gopalpoddar.textspur.features.profile.domain.usecase.GetCurrentUserProfileUseCase
import `in`.gopalpoddar.textspur.features.profile.domain.usecase.LogoutUseCase
import `in`.gopalpoddar.textspur.features.profile.domain.usecase.ReauthenticateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val reauthenticateUseCase: ReauthenticateUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val uid = authRepository.getCurrentUserId()
            if (uid != null) {
                val result = getCurrentUserProfileUseCase()
                result.onSuccess { profile ->
                    _state.update { it.copy(userProfile = profile, isLoading = false) }
                }.onFailure { e ->
                    _state.update { it.copy(error = e.message, isLoading = false) }
                }
            } else {
                _state.update { it.copy(error = "User not logged in", isLoading = false) }
            }
        }
    }

    fun setShowLogoutDialog(show: Boolean) {
        _state.update { it.copy(showLogoutDialog = show) }
    }

    fun setShowDeleteWarningDialog(show: Boolean) {
        _state.update { it.copy(showDeleteWarningDialog = show) }
    }

    fun setShowReauthenticateDialog(show: Boolean) {
        _state.update { it.copy(showReauthenticateDialog = show, showDeleteWarningDialog = false) }
    }
    
    fun dismissError() {
        _state.update { it.copy(error = null) }
    }

    fun logout() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, showLogoutDialog = false) }
            val result = logoutUseCase()
            result.onSuccess {
                _state.update { it.copy(isLogoutSuccess = true, isLoading = false) }
            }.onFailure { e ->
                _state.update { it.copy(error = e.message ?: "Failed to logout", isLoading = false) }
            }
        }
    }

    fun reauthenticateAndDelete(password: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, showReauthenticateDialog = false, error = null) }
            val reauthResult = reauthenticateUseCase(password)
            if (reauthResult.isSuccess) {
                val deleteResult = deleteAccountUseCase()
                deleteResult.onSuccess {
                    _state.update { it.copy(isDeleteAccountSuccess = true, isLoading = false) }
                }.onFailure { e ->
                    _state.update { it.copy(error = e.message ?: "Failed to delete account", isLoading = false) }
                }
            } else {
                _state.update { it.copy(error = reauthResult.exceptionOrNull()?.message ?: "Authentication failed", isLoading = false) }
            }
        }
    }
}
