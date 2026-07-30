package `in`.gopalpoddar.textspur.features.profile.saveprofile.presentation

data class SaveProfileState(
    val name: String = "",
    val email: String = "",
    val username: String = "",
    val uid: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
