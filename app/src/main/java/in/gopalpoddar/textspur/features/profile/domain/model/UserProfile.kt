package `in`.gopalpoddar.textspur.features.profile.domain.model

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val username: String = "",
    val isOnline: Boolean = false,
    val isVerified: Boolean = false
)
