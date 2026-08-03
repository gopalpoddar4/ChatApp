package `in`.gopalpoddar.textspur.features.profile.presentation

import `in`.gopalpoddar.textspur.features.profile.domain.model.UserProfile

data class ProfileState(
    val userProfile: UserProfile? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLogoutSuccess: Boolean = false,
    val isDeleteAccountSuccess: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val showDeleteWarningDialog: Boolean = false,
    val showReauthenticateDialog: Boolean = false
)
