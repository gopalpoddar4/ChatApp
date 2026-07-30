package `in`.gopalpoddar.textspur.features.auth.signup.presentation

import `in`.gopalpoddar.textspur.features.auth.signup.domain.model.CreateAccountResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.gopalpoddar.textspur.features.auth.signup.domain.usecase.CreateAccountUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val createAccountUseCase: CreateAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpState())
    val uiState: StateFlow<SignUpState> = _uiState.asStateFlow()

    fun signUp(name: String, email: String, password: String) {
        if (_uiState.value.isLoading) return

        _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false, isProfileSaveRequired = false) }

        viewModelScope.launch {
            when (val result = createAccountUseCase(name, email, password)) {
                is CreateAccountResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                is CreateAccountResult.ProfileSaveFailed -> {
                    _uiState.update { it.copy(isLoading = false, isProfileSaveRequired = true) }
                }
                is CreateAccountResult.AuthError -> {
                    _uiState.update { it.copy(isLoading = false, error = result.exception.message ?: "An unknown error occurred") }
                }
            }
        }
    }

    fun resetError() {
        _uiState.update { it.copy(error = null) }
    }
}
