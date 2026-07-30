package `in`.gopalpoddar.textspur.features.profile.saveprofile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.gopalpoddar.textspur.features.profile.domain.usecase.CreateUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveProfileViewModel @Inject constructor(
    private val createUserProfileUseCase: CreateUserProfileUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(SaveProfileState())
    val uiState: StateFlow<SaveProfileState> = _uiState.asStateFlow()

    init {
        val user = firebaseAuth.currentUser
        if (user != null) {
            val email = user.email ?: ""
            val name = user.displayName ?: ""
            val uid = user.uid
            val username = email.substringBefore("@")
            
            _uiState.update {
                it.copy(
                    name = name,
                    email = email,
                    uid = uid,
                    username = username
                )
            }
        } else {
            _uiState.update { it.copy(error = "No authenticated user found.") }
        }
    }

    fun saveProfile() {
        if (_uiState.value.isLoading) return
        
        val currentState = _uiState.value
        if (currentState.uid.isBlank()) {
            _uiState.update { it.copy(error = "User ID is missing.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }

        viewModelScope.launch {
            val result = createUserProfileUseCase(
                uid = currentState.uid,
                name = currentState.name,
                email = currentState.email
            )
            
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }
            result.onFailure { exception ->
                _uiState.update { it.copy(isLoading = false, error = exception.message ?: "Unable to save your profile. Please check your internet connection and try again.") }
            }
        }
    }

    fun resetError() {
        _uiState.update { it.copy(error = null) }
    }
}
